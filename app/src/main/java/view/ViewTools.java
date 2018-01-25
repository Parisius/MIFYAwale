package view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 *  Utility class for views.
 * Created by Parikiri13 on 24/01/2018.
 */

public class ViewTools {

    /**
     * Inflates the given view.
     */
    public static View inflateView(Context context, int resource) {
        LayoutInflater vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return vi.inflate(resource, null);
    }

    /**
     * Retrieves the id of a resource from a given package.
     *
     * @return the id or -1 if not found
     */
    public static int getResourceId(Context context, String packageName,
                                    String name, String defType) {
        try {
            return context.getPackageManager().getResourcesForApplication(
                    packageName).getIdentifier(name, defType, packageName);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return 0;
    }

    /**
     * Retrieves the array of a resource from a given package.
     *
     * @return the array or <code>null</code> if not found
     */
    public static int[] getIntArray(Context context, String packageName,
                                    String name, String defType) {
        try {
            return context.getPackageManager().getResourcesForApplication(
                    packageName).getIntArray(
                    getResourceId(context, packageName, name, defType));
        } catch (PackageManager.NameNotFoundException e) {

        }
        return null;
    }

    private static final HashMap<String, Context> CONTEXTS = new HashMap<String, Context>();

    public static Context getPackageContext(Context context, String packageName) {

        return getPackageContext(context, packageName, false);
    }

    /**
     * Retrieves the context for a package.
     *
     * @return the package context or <code>null</code> if not found
     */
    public static Context getPackageContext(Context context,
                                            String packageName, boolean forceReload) {
        Context packageContext = CONTEXTS.get(packageName);
        if (packageContext != null && !forceReload) {
            return context;
        }
        try {
            packageContext = context.createPackageContext(packageName,
                    Context.CONTEXT_IGNORE_SECURITY
                            | Context.CONTEXT_INCLUDE_CODE);
            CONTEXTS.put(packageName, packageContext);
            return packageContext;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return null;
    }

    public static Drawable getDrawable(Context context, String drawable) {
        return context.getResources().getDrawable(
                ViewTools.getResourceId(context, context.getPackageName(),
                        drawable, "drawable"));
    }

    public static int getColor(Context context, String color) {
        return context.getResources().getColor(
                ViewTools.getResourceId(context, context.getPackageName(),
                        color, "color"));
    }

    public static int getDimension(Context context, String dimension) {
        return context.getResources().getDimensionPixelSize(
                ViewTools.getResourceId(context, context.getPackageName(),
                        dimension, "dimen"));
    }

    public static void changeChildStateOfParentView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                changeChildStateOfParentView(viewGroup.getChildAt(i), enabled);
            }
        }
    }
}
