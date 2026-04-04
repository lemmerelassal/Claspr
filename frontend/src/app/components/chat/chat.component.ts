import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { GrpcClientService, ChatMsg } from '../../services/grpc-client.service';
@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="chat-page">
      <div class="chat-header">
        <a routerLink="/matches" class="back-btn">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="15 18 9 12 15 6"/></svg>
        </a>
        <div class="chat-title">Chat <span class="grpc-tag">ChatService RPC</span></div>
      </div>
      <div class="messages-container" #messagesContainer>
        <div *ngFor="let msg of messages" class="message"
             [class.mine]="msg.sender_id === myId" [class.theirs]="msg.sender_id !== myId">
          <div class="bubble">{{ msg.content }}</div>
          <div class="msg-time">{{ formatTime(msg.sent_at) }}</div>
        </div>
      </div>
      <div class="chat-input">
        <input type="text" [(ngModel)]="newMessage" placeholder="Type a message..."
               (keydown.enter)="send()" />
        <button class="send-btn" (click)="send()" [disabled]="!newMessage.trim()">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor"><path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z"/></svg>
        </button>
      </div>
    </div>
  `,
  styles: [`
    .chat-page { display:flex; flex-direction:column; height:100vh; background:#0f0c29; color:#fff; max-width:480px; margin:0 auto; }
    .chat-header { display:flex; align-items:center; gap:12px; padding:12px 16px; background:rgba(255,255,255,0.04); border-bottom:1px solid rgba(255,255,255,0.06); }
    .back-btn { color:#fff; text-decoration:none; display:flex; padding:4px; }
    .chat-title { font-weight:600; font-size:1rem; display:flex; align-items:center; gap:8px; }
    .grpc-tag { padding:2px 6px; background:rgba(0,176,255,0.15); border:1px solid rgba(0,176,255,0.3); border-radius:4px; font-size:0.6rem; color:#00b0ff; font-weight:600; }
    .messages-container { flex:1; overflow-y:auto; padding:16px; display:flex; flex-direction:column; gap:8px; }
    .message { display:flex; flex-direction:column; max-width:75%; }
    .message.mine { align-self:flex-end; align-items:flex-end; }
    .message.theirs { align-self:flex-start; }
    .bubble { padding:10px 16px; border-radius:20px; font-size:0.95rem; line-height:1.4; word-break:break-word; }
    .mine .bubble { background:linear-gradient(135deg,#EE5A24,#FF6B6B); border-bottom-right-radius:6px; }
    .theirs .bubble { background:rgba(255,255,255,0.1); border-bottom-left-radius:6px; }
    .msg-time { font-size:0.7rem; color:rgba(255,255,255,0.3); margin-top:4px; padding:0 4px; }
    .chat-input { display:flex; gap:10px; padding:12px 16px; background:rgba(255,255,255,0.04); border-top:1px solid rgba(255,255,255,0.06); }
    .chat-input input { flex:1; padding:12px 18px; background:rgba(255,255,255,0.08); border:1px solid rgba(255,255,255,0.1); border-radius:24px; color:#fff; font-size:0.95rem; outline:none; }
    .chat-input input::placeholder { color:rgba(255,255,255,0.3); }
    .send-btn { width:44px; height:44px; border-radius:50%; border:none; background:linear-gradient(135deg,#EE5A24,#FF6B6B); color:#fff; cursor:pointer; display:flex; align-items:center; justify-content:center; }
    .send-btn:disabled { opacity:0.4; }
  `]
})
export class ChatComponent implements OnInit {
  messages: ChatMsg[] = [];
  newMessage = '';
  myId: string | null = null;
  private matchId = '';
  private pollTimer: any;
  @ViewChild('messagesContainer') messagesContainer!: ElementRef;

  constructor(private grpc: GrpcClientService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.myId = this.grpc.userId;
    this.matchId = this.route.snapshot.paramMap.get('matchId') || '';
    this.loadMessages();
    this.pollTimer = setInterval(() => this.loadMessages(), 3000);
  }

  ngOnDestroy(): void { clearInterval(this.pollTimer); }

  async loadMessages(): Promise<void> {
    if (!this.matchId) return;
    try {
      const res = await this.grpc.getConversation(this.matchId);
      this.messages = (res.messages || []).reverse();
      this.scrollToBottom();
    } catch (e) { console.error('GetConversation failed:', e); }
  }

  async send(): Promise<void> {
    if (!this.newMessage.trim()) return;
    try {
      await this.grpc.sendMessage(this.matchId, this.newMessage);
      this.newMessage = '';
      await this.loadMessages();
    } catch (e) { console.error('SendMessage failed:', e); }
  }

  formatTime(iso: string): string { return new Date(iso).toLocaleTimeString([], { hour:'2-digit', minute:'2-digit' }); }
  private scrollToBottom(): void {
    setTimeout(() => { if (this.messagesContainer) { const el = this.messagesContainer.nativeElement; el.scrollTop = el.scrollHeight; } }, 50);
  }
}
