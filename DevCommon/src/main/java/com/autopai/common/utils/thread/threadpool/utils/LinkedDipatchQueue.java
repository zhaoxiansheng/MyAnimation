package com.autopai.common.utils.thread.threadpool.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LinkedDipatchQueue extends LinkedList implements IDispatchWorkQueue {
    private static final String TAG = "LinkedDipatchQueue";
    private final ReentrantLock mLock = new ReentrantLock();
    private final Condition mCondition = mLock.newCondition();
    private final Sequencer mSequencer = new Sequencer();
    private boolean mClosed = false;

    @Override
    public boolean offer(Work work) {
        boolean ret = false;
        try {
            mLock.lockInterruptibly();
            ret = super.add(work);
            mCondition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mLock.unlock();
        }
        return ret;
    }

    @Override
    public boolean remove(Work work) {
        boolean ret = false;
        try {
            mLock.lockInterruptibly();
            ret = super.remove(work);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mLock.unlock();
        }
        return ret;
    }

    private Iterator getFirstValid(IPollPolicy policy){
        if(policy != null) {
            Work work = null;
            for(ListIterator iter = this.listIterator(); iter.hasNext();) {
                work = (Work)iter.next();
                Log.e(TAG, "DispatcherThreadPool travel work: " + work);
                if( policy.isValidWork(work) ) {
                    iter.previous();
                    return iter;
                }
            }
            return null;
        }else {
            Iterator it = this.iterator();
            return (it.hasNext()) ? it : null;
        }
    }

    @Override
    public Work poll(long timeout, TimeUnit unit, IPollPolicy policy) {
        Work work = null;
        long nanos = unit.toNanos(timeout);
        try {
            Iterator it = null;
            mLock.lockInterruptibly();
            Log.e(TAG, "DispatcherThreadPool begin poll >>>>>>>>>>>>>>>");
            while( (it = getFirstValid(policy)) == null && (nanos > 0L) && !mClosed) {
                nanos = mCondition.awaitNanos(nanos);
            }
            if(it != null) {
                work = (Work) it.next();
                /**
                 * record sequence running
                 */
                Integer sequenceId = work.getSequenceID();
                if (sequenceId != null) {
                    setSequenceRunning(sequenceId, (DispatchThreadPoolBase.Worker) policy);
                }
                it.remove();
            }
        } catch (InterruptedException e) {
            work = null;
            e.printStackTrace();
        }finally {
            Log.e(TAG, "<<<<<<<<<<<<<<DispatcherThreadPool Interrupt end poll " + work);
            mLock.unlock();
        }
        return work;
    }

    @Override
    public Work take(IPollPolicy policy) {
        Work work = null;
        try {
            Iterator it = null;
            mLock.lockInterruptibly();
            Log.e(TAG, "DispatcherThreadPool begin take >>>>>>>>>>>>>>>");
            do{
                while( (it = getFirstValid(policy)) == null && !mClosed) {
                    mCondition.await();
                }
                if(it != null) {
                    work = (Work)it.next();
                    /**
                     * record sequence running
                     */
                    Integer sequenceId = work.getSequenceID();
                    if (sequenceId != null) {
                        setSequenceRunning(sequenceId, (DispatchThreadPoolBase.Worker) policy);
                    }
                    it.remove();
                }
            }while (!mClosed && work == null);
        } catch (InterruptedException e) {
            work = null;
            e.printStackTrace();
        }finally {
            Log.e(TAG, "DispatcherThreadPool end take >>>>>>>>>>>>>>>");
            mLock.unlock();
        }
        return work;
    }

    @Override
    public Work peek() {
        Work work = null;
        try {
            mLock.lockInterruptibly();
            work = (Work) super.peek();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mLock.unlock();
        }
        return work;
    }

    @Override
    public boolean contains(Work work) {
        boolean ret = false;
        try {
            mLock.lockInterruptibly();
            ret = super.contains(work);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mLock.unlock();
        }
        return ret;
    }

    @Override
    public int size() {
        int size = 0;
        try {
            mLock.lockInterruptibly();
            size = super.size();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mLock.unlock();
        }
        return size;
    }

    @Override
    public void clear(){
        try {
            mLock.lockInterruptibly();
            super.clear();
            mSequencer.clear();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mLock.unlock();
        }
    }

    @Override
    public boolean lockQueue(){
        boolean ret = false;
        try {
            mLock.lockInterruptibly();
            ret = true;
        } catch (InterruptedException e) {
            ret = false;
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public void unlockQueue(){
        mLock.unlock();
    }

    @Override
    public void signal(boolean bAll) {
        if(!bAll) {
            mCondition.signal();
        }else {
            mCondition.signalAll();
        }
    }

    @Override
    public AddResult addSequence(Integer sequenceId) {
        AddResult result = null;
        try {
            mLock.lockInterruptibly();
            result = mSequencer.addSequence(sequenceId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mLock.unlock();
        }
        return result;
    }

    @Override
    public Sequencer.Sequence removeSequence(Integer sequenceId) {
        Sequencer.Sequence sequence = null;
        try {
            mLock.lockInterruptibly();
            sequence = mSequencer.removeSequence(sequenceId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mLock.unlock();
        }
        return sequence;
    }

    public boolean isSequenceRunning(Work work){
        boolean isRunning = false;

        if(work != null) {
            Integer sequenceId = work.getSequenceID();
            if(sequenceId != null) {
                mLock.lock();
                Sequencer.Sequence sequence = mSequencer.get(sequenceId);
                if( sequence != null && sequence.isRunning() ) {
                    isRunning = true;
                }
                mLock.unlock();
            }
        }
        return isRunning;
    }

    @Override
    public void setSequenceRunning(Sequencer.Sequence sequence, DispatchThreadPoolBase.Worker worker) {
        if(sequence != null) {
            try {
                mLock.lockInterruptibly();
                if(worker != null) {
                    sequence.setWorker(worker);
                }else{
                    sequence.clearWorker();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mLock.unlock();
            }
        }
    }

    @Override
    public void setSequenceRunning(Integer sequenceId, DispatchThreadPoolBase.Worker worker) {
        if(sequenceId != null) {
            try {
                mLock.lockInterruptibly();
                Sequencer.Sequence sequence = mSequencer.get(sequenceId);
                if(sequence != null) {
                    sequence.setWorker(worker);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mLock.unlock();
            }
        }
    }

    @Override
    public boolean beforeExecute(Work work) {
       return true;
    }

    @Override
    public boolean afterExecute(Work work) {
        if(work != null) {
            Integer sequenceId = work.getSequenceID();
            if (sequenceId != null) {
                mLock.lock();
                Sequencer.Sequence sequence = mSequencer.removeSequence(sequenceId);
                if (sequence != null) {
                    sequence.clearWorker();
                    mCondition.signal();
                }
                mLock.unlock();
            }
            return true;
        }

        return false;
    }

    @Override
    public List<Work> getWorks() {
        List<Work> residues = null;
        mLock.lock();
        if(this.size() > 0) {
            residues = new ArrayList<>(this);
        }
        mLock.unlock();
        return residues;
    }

    public void close(){
        try {
            mLock.lockInterruptibly();
            mClosed = true;
            mCondition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mLock.unlock();
        }
    }
}
