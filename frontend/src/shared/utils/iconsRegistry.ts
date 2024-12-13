import AdminIcon from '@/shared/components/rolesIcons/AdminIcon.vue';
import ClientIcon from '@/shared/components/rolesIcons/ClientIcon.vue';
import TrainerIcon from '@/shared/components/rolesIcons/TrainerIcon.vue';

export const roleIcons: Record<string, string> = {
  admin: AdminIcon,
  trainer: TrainerIcon,
  client: ClientIcon,
};

export const getIconByRole = (role: string): string => {
  return roleIcons[role] || roleIcons['client'];
};