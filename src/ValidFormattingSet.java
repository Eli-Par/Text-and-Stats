import java.util.*;

import javax.swing.text.StyleConstants;

public abstract class ValidFormattingSet {
    
    private static HashMap<EditorPanel.Format, Set<Object>> formattingByFileType = null;

    public static void initializeMap() {
        if(formattingByFileType != null) return;

        formattingByFileType = new HashMap<>();

        formattingByFileType.put(EditorPanel.Format.RTF, Set.of( StyleConstants.Bold, 
                                                    StyleConstants.Italic, 
                                                    StyleConstants.Underline, 
                                                    StyleConstants.FontFamily, 
                                                    StyleConstants.FontSize, 
                                                    StyleConstants.Foreground,
                                                    StyleConstants.Alignment));

        formattingByFileType.put(EditorPanel.Format.PLAIN, Set.of( ) ); 
    }

    public static boolean isFormatValid(EditorPanel.Format fileType, Object attributeKey) {
        initializeMap();

        Set<Object> set = formattingByFileType.get(fileType);
        if(set == null) return false;

        return set.contains(attributeKey);
    }
}
