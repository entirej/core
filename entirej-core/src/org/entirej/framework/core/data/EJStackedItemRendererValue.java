package org.entirej.framework.core.data;

import org.entirej.framework.core.enumerations.EJStackedItemRendererType;

public class EJStackedItemRendererValue
{
    private EJStackedItemRendererType type        = EJStackedItemRendererType.SPACER;
    private Object                    value;
    private String                    label;
    private String                    tooltip;
    private String                    lovMapping;
    private String                    actionCommand;
    private boolean                   validateLov = true;

    private String                    format;

    public void setType(EJStackedItemRendererType type)
    {
        this.type = type;
    }

    public EJStackedItemRendererType getType()
    {
        return type;
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getTooltip()
    {
        return tooltip;
    }

    public void setTooltip(String tooltip)
    {
        this.tooltip = tooltip;
    }

    public String getLovMapping()
    {
        return lovMapping;
    }

    public void setLovMapping(String lovMapping)
    {
        this.lovMapping = lovMapping;
    }

    public String getActionCommand()
    {
        return actionCommand;
    }

    public void setActionCommand(String actionCommand)
    {
        this.actionCommand = actionCommand;
    }

    public boolean isValidateLov()
    {
        return validateLov;
    }

    public void setValidateLov(boolean validateLov)
    {
        this.validateLov = validateLov;
    }

    public String getFormat()
    {
        return format;
    }

    public void setFormat(String format)
    {
        this.format = format;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EJStackedItemRendererValue other = (EJStackedItemRendererValue) obj;
        if (type != other.type)
            return false;
        if (value == null)
        {
            if (other.value != null)
                return false;
        }
        else if (!value.equals(other.value))
            return false;
        return true;
    }
    
    

}
