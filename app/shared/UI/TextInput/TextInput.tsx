import { FC } from "react";
import { GestureResponderEvent, StyleProp, TextStyle } from "react-native";
import { TextInput as MaterialInput } from "react-native-paper";

interface TextInputProps {
  value: string;
  style?: StyleProp<TextStyle>;
  changeHandler: (text: string) => void;
  icon?: string;
  pressHandler?: (event: GestureResponderEvent) => void;
}

const TextInput: FC<TextInputProps> = ({
  value,
  style,
  changeHandler,
  icon,
  pressHandler,
}) => {
  return (
    <MaterialInput
      mode="outlined"
      style={[style, { width: "100%", fontSize: 18 }]}
      outlineStyle={{ borderRadius: 12 }}
      value={value}
      onChangeText={changeHandler}
      right={
        icon ? <MaterialInput.Icon icon={icon} onPress={pressHandler} /> : null
      }
    />
  );
};

export default TextInput;
