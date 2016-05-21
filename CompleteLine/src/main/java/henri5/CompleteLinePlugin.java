package henri5;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class CompleteLinePlugin extends AbstractUIPlugin {

  public static final String PLUGIN_ID = "CompleteLine";
  private static CompleteLinePlugin plugin;

  public CompleteLinePlugin() {
  }

  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);
    plugin = this;
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    plugin = null;
    super.stop(context);
  }

  public static CompleteLinePlugin getDefault() {
    return plugin;
  }
}
