import { useMemo } from "react";
import data from "@/app/shared/data/devices.json";
import { IDevice } from "../types";

const deviceMap = new Map<number, IDevice>();

data.forEach((d) => deviceMap.set(d.id, d));

export const useDevices = () => {
  const devices = useMemo(() => data as IDevice[], []);

  const getById = (id: number) => deviceMap.get(id);

  return {
    devices,
    getById,
  };
};
