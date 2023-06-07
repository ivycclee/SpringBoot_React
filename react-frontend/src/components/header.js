import React, {Component} from 'react';
import '../styles/header.css';
import {Link} from 'react-router-dom';
import {Navbar, NavbarBrand} from 'reactstrap';


export default class Navigation extends Component {
    render() {
        return <Navbar className="navbar navbar-expand-md navbar-light">
            <NavbarBrand tag={Link} to="/" className="nav-link">Home</NavbarBrand>
        </Navbar>;
    }
}