package org.entirej.framework.core.data;

import org.entirej.framework.core.enumerations.EJStackedItemRendererType;

public class EJStackedItemRendererConfig
{
    private final EJStackedItemRendererType type;

    private EJStackedItemRendererConfig(EJStackedItemRendererType type)
    {
        this.type = type;
    }

    private String label;
    private String tooltip;

    public EJStackedItemRendererType getType()
    {
        return type;
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

    public static class LOVSupportConfig extends EJStackedItemRendererConfig
    {

        private LOVSupportConfig(EJStackedItemRendererType type)
        {
            super(type);

        }

        private String  lovMapping;

        private boolean validateLov = true;

        public String getLovMapping()
        {
            return lovMapping;
        }

        public void setLovMapping(String lovMapping)
        {
            this.lovMapping = lovMapping;
        }

        public boolean isValidateLov()
        {
            return validateLov;
        }

        public void setValidateLov(boolean validateLov)
        {
            this.validateLov = validateLov;
        }
    }

    public static class Text extends LOVSupportConfig
    {
        public Text()
        {
            super(EJStackedItemRendererType.TEXT);
        }
    }

    public static class Label extends EJStackedItemRendererConfig
    {
        public Label()
        {
            super(EJStackedItemRendererType.LABEL);
        }
    }

    public static class Spacer extends EJStackedItemRendererConfig
    {
        public Spacer()
        {
            super(EJStackedItemRendererType.SPACER);
        }
    }

    public static class Date extends LOVSupportConfig
    {
        public Date()
        {
            super(EJStackedItemRendererType.DATE);
        }

        private String format;

        public String getFormat()
        {
            return format;
        }

        public void setFormat(String format)
        {
            this.format = format;
        }
    }

    public static class Number extends LOVSupportConfig
    {
        public Number()
        {
            super(EJStackedItemRendererType.NUMBER);
        }

        private String format;

        public String getFormat()
        {
            return format;
        }

        public void setFormat(String format)
        {
            this.format = format;
        }
    }

    public static class CheckBox extends ActionSupportConfig
    {
        public CheckBox()
        {
            super(EJStackedItemRendererType.CHECKBOX);
        }

        private Object checkBoxCheckedValue;
        private Object checkBoxUnCheckedValue;

        public Object getCheckBoxCheckedValue()
        {
            return checkBoxCheckedValue;
        }

        public void setCheckBoxCheckedValue(Object checkBoxCheckedValue)
        {
            this.checkBoxCheckedValue = checkBoxCheckedValue;
        }

        public Object getCheckBoxUnCheckedValue()
        {
            return checkBoxUnCheckedValue;
        }

        public void setCheckBoxUnCheckedValue(Object checkBoxUnCheckedValue)
        {
            this.checkBoxUnCheckedValue = checkBoxUnCheckedValue;
        }
    }

    public static class ActionSupportConfig extends EJStackedItemRendererConfig
    {

        private ActionSupportConfig(EJStackedItemRendererType type)
        {
            super(type);

        }

        private String actionCommand;

        public String getActionCommand()
        {
            return actionCommand;
        }

        public void setActionCommand(String actionCommand)
        {
            this.actionCommand = actionCommand;
        }
    }

}
