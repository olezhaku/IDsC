import { FC } from "react";
import { StyleProp, StyleSheet, View, ViewStyle } from "react-native";
import { MD3Theme, useTheme } from "react-native-paper";

interface LayoutProps {
  style?: StyleProp<ViewStyle>;
  children?: React.ReactNode;
}

const Layout: FC<LayoutProps> = ({ style, children }) => {
  const theme = useTheme();
  const styles = getStyles(theme);

  return <View style={[style, styles.container]}>{children}</View>;
};

export default Layout;

const getStyles = (theme: MD3Theme) =>
  StyleSheet.create({
    container: {
      flex: 1,
      paddingRight: 20,
      paddingLeft: 20,
      paddingTop: 20,
      backgroundColor: theme.colors.background,
    },
  });
