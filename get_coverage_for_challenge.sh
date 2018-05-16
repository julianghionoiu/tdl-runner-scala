#!/usr/bin/env bash

set -e
set -u
set -o pipefail

SCRIPT_CURRENT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

CHALLENGE_ID=$1
SCOVERAGE_REPORT_XML_FILE="${SCRIPT_CURRENT_DIR}/target/scala-2.12/scoverage-report/scoverage.xml"
SCALA_CODE_COVERAGE_INFO="${SCRIPT_CURRENT_DIR}/target/scala-code-coverage.txt"

sbt clean coverage test || true 1>&2
sbt coverageReport || true 1>&2

[ -e ${SCALA_CODE_COVERAGE_INFO} ] && rm ${SCALA_CODE_COVERAGE_INFO}

if [ -f "${SCOVERAGE_REPORT_XML_FILE}" ]; then
    COVERAGE_OUTPUT=$(xmllint --xpath '//package[@name="befaster.solutions.'${CHALLENGE_ID}'"]/@statement-rate' ${SCOVERAGE_REPORT_XML_FILE})
    COVERAGE_PERCENT=$(echo ${COVERAGE_OUTPUT} | tr '".' ' ' | awk '{print $2}')
    COVERAGE_PERCENT=$(expr ${COVERAGE_PERCENT})
    echo ${COVERAGE_PERCENT} > ${SCALA_CODE_COVERAGE_INFO}
    cat ${SCALA_CODE_COVERAGE_INFO}
    exit 0
else
    exit -1
fi
