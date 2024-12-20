package org.yearup.data;


import org.yearup.models.Profile;
import org.yearup.models.User;

public interface ProfileDao
{
    Profile getProfileByUserId(int userId);
    Profile update(Profile updateProfile);
    Profile create(Profile profile);
}
