import React, { useEffect, useState } from "react";
import { useTable, useSortBy } from "react-table";
import { LIST } from './constants/pageConstants';

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

const checkboxes = {
    name: "name",
    location: "location",
    startDate: "startDate",
    endDate: "endDate"
};

function App() {
    const [data, setData] = useState();
    const [displayedData, setDisplayedData] = useState([]);
    const [query, setQuery] = useState();

    const columns = React.useMemo(
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
        ],
        []
    );

    const memoData= React.useMemo(
        () => {
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

            if(data !== undefined && data !== null){
                setDisplayedData(Object.values(data));
                return Object.values(data);
            }
            else {
                setDisplayedData(fillerData);
                return fillerData;
            }
        },
        [data]
    );

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

    const handleCheck = (e) => {
        for( let item in checkboxes){
            if(item !== e.target.id) {
                let checkbox = document.getElementById(item);
                if(checkbox.hasAttribute("disabled")) checkbox.removeAttribute("disabled");
                else checkbox.setAttribute("disabled","");
            }
        }
    }

    const handleClick = (e) => {
        console.log("gettting clicked");
        let checkboxes = document.getElementsByClassName("check");
        let checked = undefined;
        let events = [];
        for(let checkbox of checkboxes){
            if(checkbox.checked)checked = checkbox.id;
        }

        if(checked === undefined || query === undefined || query === "")
            alert("you need to select one of the checkboxes to filter your search Or" +
                "you need to input your query into the input box.");
        else {
            for(let event of memoData){
                if(event[checked] === query)events.push(event);
            }

            setDisplayedData(events);
            if(events.length === 0)alert("There are no records with that query." +
                "try a different input.");
        }

    };

    return (
        <div>
            <p> You can sort by the following checkboxes and the value of your search</p>
            <p>Ex. to search by start date click the start date checkbox and enter the date in the input box. Ex. ( "2022-02-12")</p>
            <input type="checkbox" id="name" className="check" onChange={handleCheck} />Name
            <input type="checkbox" id="location" className="check" onChange={handleCheck} />Location
            <input type="checkbox" id="startDate" className="check" onChange={handleCheck} />startDate
            <input type="checkbox" id="endDate" className="check" onChange={handleCheck} />EndDate
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
