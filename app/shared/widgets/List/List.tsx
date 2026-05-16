import { FC } from "react";
import { TouchableOpacity, View } from "react-native";
import { Divider, List as MaterialList } from "react-native-paper";

import { IDevice } from "@/app/shared/types";

interface ListProps {
  device: IDevice;
  pressHandler: (device: number) => void;
  showDivider?: boolean;
}

const List: FC<ListProps> = ({ device, pressHandler, showDivider = false }) => {
  return (
    <View>
      <TouchableOpacity onPress={() => pressHandler(device.id)}>
        <MaterialList.Item
          title={`${device.id}. ${device.manufacturer} | ${device.marketing_name} | ${device.chipset}`}
          titleNumberOfLines={2}
          titleStyle={{ lineHeight: 24 }}
        />
      </TouchableOpacity>

      {showDivider && <Divider />}
    </View>
  );
};

export default List;
