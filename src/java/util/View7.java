package util;

/** Escape untrusted text before placing it in HTML text or quoted attributes. */
public final class View7 {
    private View7() { }
    public static String html(Object value) {
        return value == null ? "" : value.toString().replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
    }
    public static <T> java.util.List<T> list(Object value, Class<T> type) {
        if (value == null) return java.util.Collections.emptyList();
        if (!(value instanceof java.util.List<?>)) throw new IllegalArgumentException("Attribute không phải danh sách");
        java.util.List<T> result = new java.util.ArrayList<>();
        for (Object item : (java.util.List<?>) value) result.add(type.cast(item));
        return java.util.Collections.unmodifiableList(result);
    }
}
