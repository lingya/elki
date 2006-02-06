package de.lmu.ifi.dbs.evaluation.holdout;

import de.lmu.ifi.dbs.data.MetricalObject;
import de.lmu.ifi.dbs.database.Database;
import de.lmu.ifi.dbs.utilities.optionhandling.Parameterizable;

/**
 * A holdout procedure is to provide a range of partitions of
 * a database to pairs of training and test data sets.
 * 
 * @author Arthur Zimek (<a href="mailto:zimek@dbs.ifi.lmu.de">zimek@dbs.ifi.lmu.de</a>)
 */
public interface Holdout<M extends MetricalObject> extends Parameterizable
{
    /**
     * Provides a range of partitions of
     * a database to pairs of training and test data sets.
     * 
     * 
     * @param database the database to partition
     * @return a range of partitions of
     * a database to pairs of training and test data sets
     */
    public TrainingAndTestSet<M>[] partition(Database<M> database);
    
    /**
     * Sets the class labels occuring in the given database
     * to this holdout.
     * 
     * 
     * @param database the database to take all class labels from
     */
    public void setClassLabels(Database<M> database);
    
    /**
     * Returns the complete database as it has been set in
     * the partition method.
     * 
     * 
     * @return the complete database as it has been set in
     * the partition method
     */
    public Database<M> completeData();
}
