package de.lmu.ifi.dbs.elki.distance.distancefunction.geo;

/*
 This file is part of ELKI:
 Environment for Developing KDD-Applications Supported by Index-Structures

 Copyright (C) 2013
 Ludwig-Maximilians-Universität München
 Lehr- und Forschungseinheit für Datenbanksysteme
 ELKI Development Team

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.data.spatial.SpatialComparable;
import de.lmu.ifi.dbs.elki.data.type.SimpleTypeInformation;
import de.lmu.ifi.dbs.elki.data.type.VectorFieldTypeInformation;
import de.lmu.ifi.dbs.elki.database.query.distance.SpatialPrimitiveDistanceQuery;
import de.lmu.ifi.dbs.elki.database.relation.Relation;
import de.lmu.ifi.dbs.elki.distance.distancefunction.AbstractVectorDoubleDistanceFunction;
import de.lmu.ifi.dbs.elki.distance.distancefunction.SpatialPrimitiveDoubleDistanceFunction;
import de.lmu.ifi.dbs.elki.distance.distancevalue.DoubleDistance;
import de.lmu.ifi.dbs.elki.math.geodesy.EarthModel;
import de.lmu.ifi.dbs.elki.math.geodesy.SphericalVincentyEarthModel;
import de.lmu.ifi.dbs.elki.utilities.documentation.Reference;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.AbstractParameterizer;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.OptionID;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.constraints.GreaterEqualConstraint;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.constraints.NoDuplicateValueGlobalConstraint;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.parameterization.Parameterization;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.parameters.IntParameter;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.parameters.ObjectParameter;

/**
 * Distance function for 2D vectors in Latitude, Longitude form.
 * 
 * @author Erich Schubert
 */
public class DimensionSelectingLatLngDistanceFunction extends AbstractVectorDoubleDistanceFunction implements SpatialPrimitiveDoubleDistanceFunction<NumberVector<?>> {
  /**
   * Latitude dimension.
   */
  final int dimlat;

  /**
   * Longitude dimension.
   */
  final int dimlng;

  /**
   * Earth model used.
   */
  final EarthModel model;

  /**
   * Constructor.
   * 
   * @param dimlat Dimension storing the latitude
   * @param dimlng Dimension storing the longitude
   * @param model Earth model
   */
  public DimensionSelectingLatLngDistanceFunction(int dimlat, int dimlng, EarthModel model) {
    super();
    this.dimlat = dimlat;
    this.dimlng = dimlng;
    this.model = model;
  }

  @Override
  public double doubleDistance(NumberVector<?> o1, NumberVector<?> o2) {
    return model.distanceDeg(o1.doubleValue(dimlat), o1.doubleValue(dimlng), o2.doubleValue(dimlat), o2.doubleValue(dimlng));
  }

  @Override
  @Reference(authors = "Erich Schubert, Arthur Zimek and Hans-Peter Kriegel", title = "Geodetic Distance Queries on R-Trees for Indexing Geographic Data", booktitle = "Advances in Spatial and Temporal Databases - 13th International Symposium, SSTD 2013, Munich, Germany")
  public double doubleMinDist(SpatialComparable mbr1, SpatialComparable mbr2) {
    if (mbr1 instanceof NumberVector) {
      if (mbr2 instanceof NumberVector) {
        return doubleDistance((NumberVector<?>) mbr1, (NumberVector<?>) mbr2);
      } else {
        NumberVector<?> o1 = (NumberVector<?>) mbr1;
        return model.minDistDeg(o1.doubleValue(dimlat), o1.doubleValue(dimlng), mbr2.getMin(dimlat), mbr2.getMin(dimlng), mbr2.getMax(dimlat), mbr2.getMax(dimlng));
      }
    } else {
      if (mbr2 instanceof NumberVector) {
        NumberVector<?> o2 = (NumberVector<?>) mbr2;
        return model.minDistDeg(o2.doubleValue(dimlat), o2.doubleValue(dimlng), mbr1.getMin(dimlat), mbr1.getMin(dimlng), mbr1.getMax(dimlat), mbr1.getMax(dimlng));
      } else {
        throw new UnsupportedOperationException("MBR to MBR mindist is not yet implemented.");
      }
    }
  }

  @Override
  public DoubleDistance minDist(SpatialComparable mbr1, SpatialComparable mbr2) {
    return new DoubleDistance(doubleMinDist(mbr1, mbr2));
  }

  @Override
  public SimpleTypeInformation<? super NumberVector<?>> getInputTypeRestriction() {
    return new VectorFieldTypeInformation<>(NumberVector.class, Math.max(dimlat, dimlng), Integer.MAX_VALUE);
  }

  @Override
  public <T extends NumberVector<?>> SpatialPrimitiveDistanceQuery<T, DoubleDistance> instantiate(Relation<T> relation) {
    return new SpatialPrimitiveDistanceQuery<>(relation, this);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + dimlat;
    result = prime * result + dimlng;
    result = prime * result + ((model == null) ? 0 : model.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    DimensionSelectingLatLngDistanceFunction other = (DimensionSelectingLatLngDistanceFunction) obj;
    if (dimlat != other.dimlat) {
      return false;
    }
    if (dimlng != other.dimlng) {
      return false;
    }
    if (model == null) {
      if (other.model != null) {
        return false;
      }
    } else if (!model.equals(other.model)) {
      return false;
    }
    return true;
  }

  /**
   * Parameterization class.
   * 
   * @author Erich Schubert
   * 
   * @apiviz.exclude
   */
  public static class Parameterizer extends AbstractParameterizer {
    /**
     * Latitude dimension parameter.
     */
    public static final OptionID LATDIM_ID = new OptionID("distance.latitudedim", "The dimension containing the latitude.");

    /**
     * Longitude dimension parameter.
     */
    public static final OptionID LNGDIM_ID = new OptionID("distance.longitudedim", "The dimension containing the longitude.");

    /**
     * Latitude dimension.
     */
    int dimlat;

    /**
     * Longitude dimension.
     */
    int dimlng;

    /**
     * Earth model used.
     */
    EarthModel model;

    @Override
    protected void makeOptions(Parameterization config) {
      super.makeOptions(config);
      final IntParameter dimlatP = new IntParameter(LATDIM_ID);
      dimlatP.addConstraint(new GreaterEqualConstraint(0));
      if (config.grab(dimlatP)) {
        dimlat = dimlatP.getValue();
      }
      final IntParameter dimlngP = new IntParameter(LNGDIM_ID);
      dimlngP.addConstraint(new GreaterEqualConstraint(0));
      if (config.grab(dimlngP)) {
        dimlng = dimlngP.getValue();
      }
      config.checkConstraint(new NoDuplicateValueGlobalConstraint(dimlatP, dimlngP));
      ObjectParameter<EarthModel> modelP = new ObjectParameter<>(EarthModel.MODEL_ID, EarthModel.class, SphericalVincentyEarthModel.class);
      if (config.grab(modelP)) {
        model = modelP.instantiateClass(config);
      }
    }

    @Override
    protected DimensionSelectingLatLngDistanceFunction makeInstance() {
      return new DimensionSelectingLatLngDistanceFunction(dimlat, dimlng, model);
    }
  }
}
