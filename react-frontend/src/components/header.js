import React from 'react';
import './header.css';


function Navigation() {
    return (
    <div>
        <header>
            <nav className="navbar navbar-expand-md navbar-light">
                <button className="navbar-toggler" type="button" data-toggle="collapse"
                        data-target="#collapsibleNavbar">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="collapsibleNavbar">
                    <ul className="navbar-nav">
                        <li className="nav-item">
                            <a className="nav-link" href="/">Home</a>
                        </li>
                    </ul>
                </div>
            </nav>
        </header>
    </div>
    );
}
export default Navigation;