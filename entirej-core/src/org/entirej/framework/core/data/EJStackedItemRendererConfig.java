package org.entirej.framework.core.data;

import java.util.ArrayList;
import java.util.List;

import org.entirej.framework.core.enumerations.EJStackedItemRendererType;

public class EJStackedItemRendererConfig
{

    public static final int                 DEFUALT = 0;
    private final EJStackedItemRendererType type;

    private EJStackedItemRendererConfig(EJStackedItemRendererType type)
    {
        this.type = type;
    }

    private String  label;
    private String  tooltip;

    private int     xSpan = DEFUALT;
    private int     ySpan = DEFUALT;

    private boolean expandHorizontally;
    private boolean expandVertically;

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

    
    
    
    public int getXSpan()
    {
        return xSpan;
    }

    public void setXSpan(int xSpan)
    {
        this.xSpan = xSpan;
    }

    public int getYSpan()
    {
        return ySpan;
    }

    public void setYSpan(int ySpan)
    {
        this.ySpan = ySpan;
    }

    public boolean isExpandHorizontally()
    {
        return expandHorizontally;
    }

    public void setExpandHorizontally(boolean expandHorizontally)
    {
        this.expandHorizontally = expandHorizontally;
    }

    public boolean isExpandVertically()
    {
        return expandVertically;
    }

    public void setExpandVertically(boolean expandVertically)
    {
        this.expandVertically = expandVertically;
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

    public static class TextArea extends LOVSupportConfig
    {
        public TextArea()
        {
            super(EJStackedItemRendererType.TEXT_AREA);
        }

        private int lines = 3;

        public int getLines()
        {
            return lines;
        }

        public void setLines(int lines)
        {
            this.lines = lines;
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

    public static class Combo extends LOVSupportConfig
    {

        private List<Column> entries = new ArrayList<Column>();

        public Combo()
        {
            super(EJStackedItemRendererType.COMBO);
        }

        private int    visibleItemCount = -1;

        private String lovDefinition;
        private String itemName;

        public String getLovDefinition()
        {
            return lovDefinition;
        }

        public String getItemName()
        {
            return itemName;
        }

        public void setItemName(String itemName)
        {
            this.itemName = itemName;
        }

        public void setLovDefinition(String lovDefinition)
        {
            this.lovDefinition = lovDefinition;
        }

        public int getVisibleItemCount()
        {
            return visibleItemCount;
        }

        public void setVisibleItemCount(int visibleItemCount)
        {
            this.visibleItemCount = visibleItemCount;
        }

        public List<Column> getColumns()
        {
            return entries;
        }

        public void addColumn(String item, boolean displayed)
        {
            addColumn(item, displayed, null, null);
        }

        public void addColumn(String item, boolean displayed, String returnItem)
        {
            addColumn(item, displayed, returnItem, null);
        }

        public void addColumn(String item, boolean displayed, String returnItem, String datatypeFormat)
        {
            Column column = new Column();

            column.datatypeFormat = datatypeFormat;
            column.displayed = displayed;
            column.item = item;
            column.returnItem = returnItem;
            entries.add(column);
        }

        public static class Column
        {
            private String  item;
            private boolean displayed;
            private String  datatypeFormat;
            private String  returnItem;

            private Column()
            {

            }

            public String getDatatypeFormat()
            {
                return datatypeFormat;
            }

            public String getItem()
            {
                return item;
            }

            public String getReturnItem()
            {
                return returnItem;
            }

            public boolean isDisplayed()
            {
                return displayed;
            }

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

    public static class Button extends ActionSupportConfig
    {
        public Button()
        {
            super(EJStackedItemRendererType.BUTTON);
        }

        private String image;

        public String getImage()
        {
            return image;
        }

        public void setImage(String image)
        {
            this.image = image;
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
