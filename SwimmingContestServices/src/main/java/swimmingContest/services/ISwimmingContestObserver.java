package swimmingContest.services;

import swimmingContest.model.SignUpDTO;

public interface ISwimmingContestObserver {
    void addedSignUp(SignUpDTO signUpDTO) throws SwimmingContestException;
}
