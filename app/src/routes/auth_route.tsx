import React, { useEffect, useState } from 'react';
import { Redirect, Route } from 'react-router-dom';
import { RouteProps } from 'src/routes/data';
import useLocalStorage from 'src/utils/storage';

export interface AuthRouteProps {
    route: RouteProps;
}

const AuthRoute = ({ route }: AuthRouteProps) => {
    const { storedValue } = useLocalStorage('user');
    const [isLoggedIn, setIsLoggedIn] = useState(() => !!storedValue);

    useEffect(() => {
        setIsLoggedIn(!!storedValue);
    }, [storedValue]);

    return isLoggedIn ?
        <Route exact path={route.path} component={route.component} /> :
        <Redirect to="/login" />;

};

export default AuthRoute;
