package com.dating.service.impl;

import com.dating.entity.UserProfile;
import com.dating.entity.UserProfile.Gender;
import com.dating.service.GenderPreferenceFilter;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class GenderPreferenceFilterImpl implements GenderPreferenceFilter {

    @Override
    public List<Gender> getAllowedGenders(UserProfile user) {
        var genders = new ArrayList<Gender>();
        if (user.showMen)       genders.add(Gender.MALE);
        if (user.showWomen)     genders.add(Gender.FEMALE);
        if (user.showMtfTrans)  genders.add(Gender.MTF_TRANS);
        if (user.showFtmTrans)  genders.add(Gender.FTM_TRANS);
        if (user.showNonBinary) genders.add(Gender.NON_BINARY);
        genders.add(Gender.OTHER); // always included so nobody gets hidden
        return Collections.unmodifiableList(genders);
    }
}
