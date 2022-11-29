package org.nocrala.tools.gis.data.esri.shapefile.shape.shapes;

import org.nocrala.tools.gis.data.esri.shapefile.ValidationPreferences;
import org.nocrala.tools.gis.data.esri.shapefile.exception.InvalidShapeFileException;
import org.nocrala.tools.gis.data.esri.shapefile.shape.ShapeHeader;
import org.nocrala.tools.gis.data.esri.shapefile.shape.ShapeType;

import java.io.IOException;
import java.io.InputStream;

/**
 * Represents a Polygon Shape object, as defined by the ESRI Shape file
 * specification.
 */
public class PolygonShape extends AbstractPolyPlainShape {

    public PolygonShape(final ShapeHeader shapeHeader, final ShapeType shapeType,
                        final InputStream is, final ValidationPreferences rules)
            throws IOException, InvalidShapeFileException {

        super(shapeHeader, shapeType, is, rules);

    }

    @Override
    protected String getShapeTypeName() {
        return "Polygon";
    }

}
