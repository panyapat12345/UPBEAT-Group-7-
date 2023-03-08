import React from 'react'
import Link from 'next/link'


function SetConstructionPlanPlayer1() {
  return (
    <div>
          <li>
        <Link href="/">Home</Link>
      </li>
      <h1>SetConstructionPlanPlayer1</h1>
      <li>
       <textarea
          rows={30}
          cols={100}
        ></textarea>
      </li>
      <li>
        <button>
        <Link href="/Part1SetupGame/SetConfigurationPlan">Back</Link>
        </button>
      </li>
      <li>
        <button>
        <Link href="/Part1SetupGame/SetConstructionPlanPlayer2">Next</Link>
        </button>
      </li>
    </div>
  )
}

export default SetConstructionPlanPlayer1