import React from 'react';
import {
    Collapse,
    Navbar,
    NavbarToggler,
    NavbarBrand,
    Nav,
    NavItem,
    NavLink,
    UncontrolledDropdown,
    DropdownToggle,
    DropdownMenu,
    DropdownItem
} from 'reactstrap'
import LoginForm from './LoginForm';
import '../Styles/LoginForm.css';
import { UserConsumer } from '../Context/UserContext';

export default class Navigation extends React.Component {

    state = {
        isOpen: false
    }

    toggle = () => {
        this.setState((state) => ({
            isOpen: !state.isOpen
        }));
    }

    render() {
        return (
            <div>
                <Navbar color="light" light expand="md">
                    <NavbarBrand href="/">Q</NavbarBrand>
                    <NavbarToggler onClick={this.toggle} />
                    <Collapse isOpen={this.state.isOpen} navbar>
                        <Nav navbar>
                            <NavItem>
                                <NavLink href="/components/">Components</NavLink>
                            </NavItem>
                            <NavItem>
                                <NavLink href="https://github.com/reactstrap/reactstrap">GitHub</NavLink>
                            </NavItem>
                        </Nav>
                        <Nav className="ml-auto" navbar>
                            <UncontrolledDropdown nav inNavbar>
                                <DropdownToggle nav caret>
                                    Login
                                </DropdownToggle>
                                <DropdownMenu right>
                                    <UserConsumer>
                                    { context => 
                                        <LoginForm handleUserLogin={this.props.handleUserLogin} loginError={context.errors.loginError} />
                                    }
                                    </UserConsumer>
                                </DropdownMenu>
                            </UncontrolledDropdown>
                        </Nav>
                    </Collapse>
                </Navbar>
            </div>
        );
    }
}