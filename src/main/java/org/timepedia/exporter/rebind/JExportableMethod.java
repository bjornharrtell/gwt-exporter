package org.timepedia.exporter.rebind;

import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JAbstractMethod;
import com.google.gwt.core.ext.typeinfo.JConstructor;

/**
 * Created by IntelliJ IDEA.
 * User: ray
 * Date: Jun 11, 2007
 * Time: 5:40:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class JExportableMethod implements JExportable {
    protected JExportableClassType exportableEnclosingType;
    protected JAbstractMethod method;
    private String exportName;

    public JExportableMethod(JExportableClassType exportableEnclosingType,
                             JAbstractMethod method) {
        this.exportableEnclosingType = exportableEnclosingType;
        this.method = method;
        String metaData[][] =
                method.getMetaData(ExportableTypeOracle.GWT_EXPORT_MD);

        if (metaData != null && metaData.length > 0 && metaData[0] != null &&
                metaData[0].length > 0) {
            exportName = metaData[0][0];
        } else {
            exportName = method.getName();
        }
    }


    public String getUnqualifiedExportName() {
        return exportName;
    }
    
    public String getJSQualifiedExportName() {
        return getEnclosingExportType().getJSQualifiedExportName()+ "." +
                getUnqualifiedExportName();
    }

    public JExportableType getExportableReturnType() {
        ExportableTypeOracle xTypeOracle = getExportableTypeOracle();
        String returnTypeName =
                ( (JMethod) method ).getReturnType().getQualifiedSourceName();
        return xTypeOracle.findExportableType(returnTypeName);
    }


    public JExportableParameter[] getExportableParameters() {
        JParameter[] params = method.getParameters();
        JExportableParameter[] eparams =
                new JExportableParameter[params.length];
        int i = 0;
        for (JParameter param : params) {
            eparams[i++] = new JExportableParameter(this, param);
        }
        return eparams;
    }

    public JExportableClassType getEnclosingExportType() {
        return exportableEnclosingType;
    }

    public String getJSNIReference() {

        String reference = exportableEnclosingType.getJSNIReference() + "::" +
                method.getName() + "(";
        JParameter[] params = method.getParameters();
        for (int i = 0; i < params.length; i++) {
            reference += params[i].getType().getJNISignature();
        }
        reference += ")";
        return reference;
    }

    public boolean isStatic() {
        if (method instanceof JConstructor) {
            return false;
        } else {
            return ( (JMethod) method ).isStatic();
        }
    }


    public ExportableTypeOracle getExportableTypeOracle() {
        return getEnclosingExportType().getExportableTypeOracle();
    }

    public String toString() {
        String str=exportableEnclosingType.getQualifiedSourceName() + "." +
                method.getName() + "(";
        JExportableParameter[] params = getExportableParameters();
        for(int i=0; i<params.length; i++) {
            str += params[i];
            if(i < params.length - 1) str+=", ";
        }
        return str+")";
    }

    public String getName() {
        return method.getName();
    }
}
