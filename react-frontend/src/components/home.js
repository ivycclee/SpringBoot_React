import React, {Component} from 'react';
import '../styles/header.css';
import {Link} from 'react-router-dom';
import Navigation from "../components/header";
import {Container} from 'reactstrap';

export default class Home extends Component {
    render() {
        return (
            <div>
                <Navigation/>

                <Container fluid>
                    <div className="border border-warning p-5">
                        <h1 className="text-center pb-2">Search the db!</h1>
                        <form>
                            <div className="table-responsive">
                                <table className="mx-auto">
                                    <tr>
                                        <td className="w-50"><label for="start">Start Date</label></td>
                                        <td><input type="date" required name="startdate" id="start" className="form-control"/></td>
                                    </tr>

                                    <tr>
                                        <td><label for="end">End Date</label></td>
                                        <td><input type="date" required name="enddate" id="end" className="form-control"/></td>
                                    </tr>

                                    <tr>
                                        <td><label for="country">Country</label></td>
                                        <td><input type="text" name="country" className="form-control" required/></td>
                                    </tr>

                                    <tr>
                                        <td><input type="reset" value="Clear" className="btn btn-outline-info"/>
                                        </td>
                                        <td><input type="submit" value="Submit" className="btn btn-outline-info"/>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </form>
                    </div>
                </Container>
            </div>
        )
    }
}