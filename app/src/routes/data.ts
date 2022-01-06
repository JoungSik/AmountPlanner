import React from 'react';
import Home from 'src/pages/home';

export interface RouteProps {
    path: string,
    component: React.FC
}

export const routes: Array<RouteProps> = [
    {
        path: '/',
        component: Home,
    },
];
