package com.android.prasnou.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dzianis_Prasnou on 9/16/2016.
 */
public class NewWorkoutDataObject implements Serializable{
    private int wrkNumb = -1;
    private int wrkTypeId = -1;
    private List<Ex> wrkExList = new ArrayList<>();

    public NewWorkoutDataObject(){
        super();
    }

    public NewWorkoutDataObject(int numb, int typeId){
        super();
        wrkNumb = numb;
        wrkTypeId = typeId;
    }


    public void setWrkNumb(int numb){
        wrkNumb = numb;
    }

    public int getWrkNumb(){
        return wrkNumb;
    }

    public int getWrkTypeId() {
        return wrkTypeId;
    }

    public void setWrkTypeId(int wrkTypeId) {
        this.wrkTypeId = wrkTypeId;
    }

    public List<Ex> getWrkExList() {
        return wrkExList;
    }
    public Ex newEx() {
        Ex ex = new Ex(wrkExList.size());
        wrkExList.add(ex);
        ex.setExInd(wrkExList.indexOf(ex));
        ex.setExNumb(wrkExList.size());
        return ex;
    }
    public void removeEx(int ind){
        wrkExList.remove(ind);
    }

    public int getExCount(){
        return getWrkExList().size();
    }
    //------------- Excercise ----------------------
    class Ex implements Serializable{
        private int exNumb = -1;
        private int exInd = 0;
        private List<Set> exSetList = new ArrayList<>();

        public Ex(int numb){
            super();
            exNumb = numb;
        }

        public int getExInd() {
            return exInd;
        }

        public void setExInd(int ind) {
            exInd = ind;
        }

        public int getExNumb() {
            return exNumb;
        }
        public void setExNumb(int exNumb) {
            this.exNumb = exNumb;
        }

        public List<Set> getExSetList() {
            return exSetList;
        }

        public Set newSet(int setTypeId, int weight, int reps) {
            Set set = new Set(exSetList.size()+1, setTypeId, weight, reps);
            //exSetList.add(set);
            //set.setInd(exSetList.indexOf(set));
            return set;
        }

        public void removeSet(int ind){
            exSetList.remove(ind);
        }
    }

    //------------- Set ----------------------------
    class Set{
        private int ind = 0;
        private int setNumb = -1;
        private int setTypeId = -1;
        private int setWeight = -1;
        private int setReps = -1;

        public Set(int numb, int typeId, int weight, int reps){
            setNumb = numb;
            setTypeId = typeId;
            setWeight = weight;
            setReps = reps;
        }

        public int getInd() {
            return ind;
        }

        public void setInd(int ind) {
            this.ind = ind;
        }

        public int getSetNumb() {
            return setNumb;
        }

        public void setSetNumb(int setNumb) {
            this.setNumb = setNumb;
        }

        public int getSetTypeId() {
            return setTypeId;
        }

        public void setSetTypeId(int setTypeId) {
            this.setTypeId = setTypeId;
        }

        public int getSetWeight() {
            return setWeight;
        }

        public void setSetWeight(int setWeight) {
            this.setWeight = setWeight;
        }

        public int getSetReps() {
            return setReps;
        }

        public void setSetReps(int setReps) {
            this.setReps = setReps;
        }
    }



}
