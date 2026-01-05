package my_app.screens.components;

import megalodonte.ColumnProps;
import megalodonte.ColumnStyler;
import megalodonte.ComputedState;
import megalodonte.TextStyler;
import megalodonte.components.Column;
import megalodonte.components.Component;
import megalodonte.components.SpacerVertical;
import megalodonte.components.Text;
import megalodonte.props.TextProps;
import megalodonte.props.TextVariant;
import megalodonte.theme.ThemeManager;

public class Components {

    public static Component errorText(String message){
        return new Column(new ColumnProps(), new ColumnStyler().bgColor("white")).c_child(new SpacerVertical(5))
                .c_child(new Text(message, new TextProps().variant(TextVariant.SUBTITLE), new TextStyler().color("red")));
    }
}
