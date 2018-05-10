package com.example.mohgoel.quizapp;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by MOHGOEL on 08-May-18.
 */

public class PopulateQuestions {
    private ArrayList<Question> mQuestionsList;
    private Context mContext;

    public PopulateQuestions(Context context) {
        if (context == null) {
            throw new NullPointerException("Context cannot be null to initialize PopulateQuestions instance");
        }
        this.mContext = context;
        mQuestionsList = new ArrayList<>();
    }

    public ArrayList<Question> createQuestionaire() {
        //1. Check if list is not null and not empty
        if (mQuestionsList != null && !mQuestionsList.isEmpty()){
            return mQuestionsList;
        }

        //Create Single Choice Questions
        for (int i = 1; i < 8; i++) {
            Question singleChoiceQuestion = new Question(Question.QUESTION_TYPE.SINGLE);
            singleChoiceQuestion.setId(i);
//            singleChoiceQuestion.setStatement(mContext.getString(R.string.question_1)+i);
            singleChoiceQuestion.setStatement(mContext.getString(getStringResourceIdByName("question_" + i)));
            String[] options = mContext.getResources()
                    .getStringArray(getStringArrayIdByName("options_for_question_" + i));
            singleChoiceQuestion.setOptions(options);
            singleChoiceQuestion.setCorrectAnswer(options[2]);
            singleChoiceQuestion.setImageId(getDrawableIdByName("img_question_" + i));

            mQuestionsList.add(singleChoiceQuestion);
        }
        for (int i = 8; i < 9; i++) {
            Question textQuestion = new Question(Question.QUESTION_TYPE.TEXT);
            textQuestion.setId(i);
            textQuestion.setStatement(mContext.getString(getStringResourceIdByName("question_" + i)));
            String[] options = mContext.getResources()
                    .getStringArray(getStringArrayIdByName("options_for_question_" + i));
            textQuestion.setOptions(options);
            textQuestion.setCorrectAnswer(options[3]);
            textQuestion.setImageId(getDrawableIdByName("img_question_" + i));

            mQuestionsList.add(textQuestion);
        }
        for (int i = 9; i < 11; i++) {
            Question multipleChoiceQuestion = new Question(Question.QUESTION_TYPE.MULTIPLE);
            multipleChoiceQuestion.setId(i);
            multipleChoiceQuestion.setStatement(mContext.getString(getStringResourceIdByName("question_" + i)));
            String[] options = mContext.getResources()
                    .getStringArray(getStringArrayIdByName("options_for_question_" + i));
            multipleChoiceQuestion.setOptions(options);
            multipleChoiceQuestion.setCorrectAnswer(options[3]);
            multipleChoiceQuestion.setImageId(getDrawableIdByName("img_question_" + i));

            mQuestionsList.add(multipleChoiceQuestion);
        }

        return mQuestionsList;
    }

    private int getStringResourceIdByName(String keyName) {
        String packageName = mContext.getPackageName();
        int resId = mContext.getResources().getIdentifier(keyName, "string", packageName);
        return resId;
    }
    private int getStringArrayIdByName(String keyName) {
        String packageName = mContext.getPackageName();
        int resId = mContext.getResources().getIdentifier(keyName, "array", packageName);
        return resId;
    }
    private int getDrawableIdByName(String keyName) {
        String packageName = mContext.getPackageName();
        int resId = mContext.getResources().getIdentifier(keyName, "drawable", packageName);
        return resId;
    }

    protected int getButtonIdByName(String btnID) {
        String packageName = mContext.getPackageName();
        int resId = mContext.getResources().getIdentifier(btnID, "id", packageName);
        return resId;
    }

    public Question getQuestionById(int id) {
        --id;
        return mQuestionsList.get(id);
    }

    public boolean getIsAllAttempted() {
        boolean isAllAttempted=true;
        for (int i = 0; i < mQuestionsList.size(); i++) {
            if(!mQuestionsList.get(i).isAttempted()){
                isAllAttempted=false;
                break;
            }
        }
        return isAllAttempted;
    }

    public int getScore(){
        int score=0;
        for (Question question : mQuestionsList) {
            if(question.getUserAnswer()!=null&&question.getUserAnswer().equals(question.getCorrectAnswer())){
                score++;
            }
        }
        return score;
    }
}
