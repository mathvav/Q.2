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
import UserDropdown from './UserDropdown';
import { ErrorConsumer } from '../Context/ErrorContext';

export default class Navigation extends React.Component {

    state = {
        isOpen: false
    }

    toggle = () => {
        this.setState((state) => ({
            isOpen: !state.isOpen
        }));
    }

    getUsername = (userContext) => {
        if (userContext.user) {
            return userContext.user.sub; 
        } else {
            return "Login"; 
        }
    }

    renderLoginPaneContents = (userContext, errorContext) => {
        if (userContext.user) {
            return(
            <UserDropdown user={userContext.user} handleUserLogout={this.props.handleUserLogout} />
            ); 
        } else {
            return(
                <LoginForm handleUserLogin={this.props.handleUserLogin} loginError={errorContext.loginError}/>
            );
        }
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
                                <NavLink href="#">Create New Queue</NavLink>
                            </NavItem>
                            <NavItem>
                                <NavLink href="#">My Queues</NavLink>
                            </NavItem>
                        </Nav>
                        <Nav className="ml-auto" navbar>
                            <UncontrolledDropdown nav inNavbar>
                                <DropdownToggle nav caret>
                                    <UserConsumer>
                                        { userContext => 
                                            this.getUsername(userContext)
                                        }
                                    </UserConsumer>
                                </DropdownToggle>
                                <DropdownMenu right>
                                    <UserConsumer>
                                    { userContext => (
                                        <ErrorConsumer>
                                            {
                                                errorContext => (
                                                    this.renderLoginPaneContents(userContext, errorContext)
                                                )
                                            }
                                        </ErrorConsumer>   
                                    )
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