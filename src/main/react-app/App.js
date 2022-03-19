import React, { useEffect, useState, useMemo } from "react";
import { useTable, useSortBy } from "react-table";
import { LIST } from './constants/pageConstants';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

toast.configure({
    position: "top-right",
    autoClose: 5000,
    hideProgressBar: false,
    closeOnClick: true,
    pauseOnHover: true,
    draggable: true,
    progress: undefined,
});

function Table({ columns, data }) {
    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        rows,
        prepareRow
    } = useTable({
        columns,
        data
    }, useSortBy);

    // Render the UI for your table
    return (
        <table
            {...getTableProps()}
            border={1}
            style={{ borderCollapse: "collapse", width: "100%" }}
        >
            <thead>
            {headerGroups.map((group) => (
                <tr {...group.getHeaderGroupProps()}>
                    {group.headers.map((column) => (
                        <th {...column.getHeaderProps(column.getSortByToggleProps())}>{column.render("Header")}<span>{
                            column.isSorted
                                ? column.isSortedDesc
                                    ? ' ?'
                                    : ' ?'
                                : ''
                        }</span></th>
                    ))}
                </tr>
            ))}
            </thead>
            <tbody {...getTableBodyProps()}>
            {rows.map((row, i) => {
                prepareRow(row);
                return (
                    <tr {...row.getRowProps()}>
                        {row.cells.map((cell) => {
                            return <td {...cell.getCellProps()}>{cell.render("Cell")}</td>;
                        })}
                    </tr>
                );
            })}
            </tbody>
        </table>
    );
}

const checkboxesMap = {
    name: "Name",
    location: "Location",
    startDate: "Start Date",
    endDate: "End Date"
};

function checkError(response){
    if(!response.ok) throw new Error(`MyErr:${response.status}:${response.statusText}`);
}

function sendError(e) {
    let message = "There was an error retrieving event data.";;

    if(e.message.startsWith('MyErr')) {
        let [statusCode, statusMessage] = e.message.slice(6, e.message.length).split(':');
        message = "There was an error retrieving event data.\n" +
            `statusCode:${statusCode}\n` + `statusMessage:${statusMessage}`;
    }

    const notifyError = () => toast.error(message);
    notifyError();
}

function App() {
    const [data, setData] = useState();
    const [allData, setAllData] = useState();
    const [displayedData, setDisplayedData] = useState([]);
    const [query, setQuery] = useState();
    //useMemo enables us to only render columns variable if there is a change to the dependency array
    //because the dependency array is empty this will only render columns a single time.
    const columns = useMemo(
        () => [
            {
                Header: "Events",
                columns: [
                    {
                        Header: "name",
                        accessor: "name"
                    },
                    {
                        Header: "startDate",
                        accessor: "startDate"
                    },
                    {
                        Header: "endDate",
                        accessor: "endDate"
                    },
                    {
                        Header: "location",
                        accessor: "location"
                    },
                    {
                        Header: "websiteName",
                        accessor: "websiteName"
                    }
                ]
            }
        ], []);
    //memoData will only rerender in the case of a change to the data we have received from the api call.
    const memoData = useMemo(() => {
            let fillerData = [
                {
                    name: "Row 1 Column 1",
                    startDate: "Row 1 Column 2",
                    endDate: "Row 1 Column 3",
                    location: "Row 1 Column 4",
                    websiteName: "Row 1 Column"
                },
                {
                    name: "Row 2 Column 1",
                    startDate: "Row 2 Column 2",
                    endDate: "Row 2 Column 3",
                    location: "Row 2 Column 4",
                    websiteName: "Row 2 Column"
                }
            ];

            if(allData !== undefined && allData !== null){
                setDisplayedData(Object.values(allData));
                return Object.values(data);
            }
            else {
                setDisplayedData(fillerData);
                return fillerData;
            }
        }, [allData]);

    const handleCheck = (e) => {
        for( let item in checkboxes){
            if(item !== e.target.id) {
                let checkbox = document.getElementById(item);
                if(checkbox.hasAttribute("disabled")) checkbox.removeAttribute("disabled");
                else checkbox.setAttribute("disabled","");
            }
        }
    };

    const handleClick = () => {
        let checkboxes = document.getElementsByClassName("check");
        let checked = undefined;
        let events = [];
        for(let checkbox of checkboxes){
            if(checkbox.checked)checked = checkbox.id;
        }

        if(checked === undefined || query === undefined || query === "") {
            const notifyError = () => toast.error("you need to select one of the checkboxes to filter your search Or" +
                "you need to input your query into the input box.");
            notifyError();
        }
        else {
            for(let event of memoData){
                if(event[checked] === query)events.push(event);
            }

            setDisplayedData(events);
            if(events.length === 0) {
                const notifyError = () => toast.error("There are no records with that query." +
                    "try a different input.");
                notifyError();
            }
        }

    };

    //makes modifying checkboxes easier.
    const checkboxes = useMemo(() => {
        let checkboxes = [];
        let uniqKey = 0;
        for(const [key,val] in Object.entries(checkboxesMap)){
            console.log("value",val);
            checkboxes.push(<label key={uniqKey}>{val}<input type="checkbox" id={`${key}`}
                                               className="check" onChange={handleCheck}/></label>);
            uniqKey++;
        }
        return checkboxes;
    },[checkboxesMap]);

    useEffect(() => {
        let continueProcess = true;

        const postData = async() => {
            let pages = {
                "typeOfData": "event",
                "pages": LIST
            };

            let jsonObj = JSON.stringify(pages);

            fetch('/api/crawl',{
                method:'post',
                mode:'cors',
                headers: {
                    'content-type':'application/json'
                },
                body: jsonObj
            })
                .then(response => {
                    checkError(response);
                    return response.json();
                })
                .then(json => {
                    if(continueProcess) setData(json);
                })
                .catch(e => sendError(e));
        };

        const getAllData = async() => {
            fetch('/api/getData')
                .then(response => {
                    checkError(response);

                    return response.json();
                })
                .then(json => {
                    if(continueProcess) setAllData(json);
                })
                .catch(e => sendError(e));
        };
        postData();
        getAllData();
        //prevent data from being pushed to unmounted components/memory leaks.
        return () => {
            continueProcess = false;
        };

    },[]);

    const handleChange = (e) => {
        setQuery(e.target.value);
    };


    return (
        <div>
            <p> You can sort by the following checkboxes and the value of your search</p>
            <p> Ex. to search by start date click the start date checkbox and enter the date in the input box. Ex. ( "2022-02-12")</p>
            {checkboxes}
            <p>You will be shown all events that match you query.</p>
            <input type="text" onChange={handleChange} />Filter/Sort Data
            <button type="submit" onClick={handleClick}>Submit</button>
            <Table columns={columns} data={displayedData} />
        </div>

    );
}

export default App;








/*import React, { useEffect, useState } from 'react';
import { LIST } from './constants/pageConstants';

function App() {
  const [data, setData] = useState();
  const [query, setQuery] = useState();

  useEffect(() => {
    const postData = async() => {
      let pages = {
        "typeOfData": "event",
        "pages": LIST
      };

      let jsonObj = JSON.stringify(pages);

      fetch('/api/crawl',{
        method:'post',
        mode:'cors',
        headers: {
          'content-type':'application/json'
        },
        body: jsonObj
      })
          .then(response => response.json())
          .then(json => console.log(json))
          .catch(e => console.log(e));
    };

    const getData = async() => {
      fetch('api/getData')
          .then(response => response.json())
          .then(json => setData(json))
          .catch(e => console.log(e));
    };

    postData();
    getData();

  },[]);

  const handleChange = (e) => {
    setQuery(e.target.value);
  };

  const handleClick = () => {

  };

  return (
    <div className="App">
      <p>You can request event data by start date, end date, location, name, or website.</p>
      <p>Enter a search by location by entering "Las Vegas".</p>
      <p>Enter a search by start date by entering "2022-03-30".</p>
      <p>You will be shown all events that match you query.</p>
      <p>Alternatively you can see all data sorted either ascending or descending order.</p>
      <p>Type "asc" for ascending and "desc" for descening.</p>
      <input type="text" className="input" onChange={e => handleChange}/>Filter/Sort Data
      <button onClick={handleClick}>Search</button>
    </div>
  );
}

export default App;*/
