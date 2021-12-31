import React from 'react';
import { BrowserRouter as Router, Redirect, Route, Switch } from 'react-router-dom';
import { routes } from 'src/routes/data';
import Login from 'src/pages/login';
import AuthRoute from 'src/routes/auth_route';

const MainRoutes = () => {
    return (
        <>
            <Switch>
                <Route exact path={'/login'} component={Login} />
                {
                    routes.map(route => <AuthRoute key={route.path} route={route} />)
                }
                <Redirect path="*" to="/" />
            </Switch>
        </>
    );
};


const Routers = () => {
    return (
        <Router>
            <Switch>
                <Route path={'/'} component={MainRoutes} />
            </Switch>
        </Router>
    );
};

export default Routers;