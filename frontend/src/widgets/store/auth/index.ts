import { defineStore } from 'pinia';
import { AuthController, IUserData } from "../../authController";

export const useAuthStore = defineStore('auth', {
	state: () => ({
		authToken: localStorage.getItem('authToken') || null,
		user: null as IUserData | null,
	}),
	
	getters: {
		isAuthenticated: (state) => !!state.authToken,
		userRoles: (state) => state.user?.roles || [],
	},
	
	actions: {
		// Метод для загрузки данных пользователя из токена
		loadUserDataFromToken(): void {
			const controller = new AuthController();
			this.user = controller.getUserData();
		},
		
		// Метод для получения токена
		getAuthToken(): string | null {
			const controller = new AuthController();
			const token = controller.getAuthToken();
			this.authToken = token;
			return token;
		},
		
		// Метод для установки токена
		setAuthToken(token: string): void {
			const controller = new AuthController();
			controller.setAuthToken(token);
			this.authToken = token;
			this.user = controller.getUserData();
		},
		
		// Метод для выхода
		logout(): void {
			const controller = new AuthController();
			controller.logout();
			this.authToken = null;
			this.user = null;
		},
		
		// Метод для обновления токена
		async refreshAuthToken(): Promise<string | null> {
			const controller = new AuthController();
			const token = await controller.refreshAuthToken();
			if (token) {
				this.setAuthToken(token);
			}
			return token;
		},
	},
});
