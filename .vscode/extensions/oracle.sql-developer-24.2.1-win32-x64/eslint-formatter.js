/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 * All rights reserved.
 */
"use strict";

module.exports = function (results) {
    let output = "";
    let errorCount = 0;
    let warningCount = 0;
    let fixableErrorCount = 0;
    let fixableWarningCount = 0;

    for (const result of results) {

        for (const message of result.messages) {

            const location = `${result.filePath}:${message.line}:${message.column}`;
        
            let messageType;
            if (message.fatal || message.severity === 2) {
                messageType = "Error";
            } else {
                messageType = "Warning";
            }
        
            output += `\n${messageType} at ${location}\n${message.message}\n`;
        
            if (message.suggestions) {
                const suggestions = message.suggestions.map(suggestion => `- ${suggestion.desc}`).join("\n");
                output += `Suggestions:\n${suggestions}\n`;
            }

        }

        errorCount += result.errorCount;
        warningCount += result.warningCount;
        fixableErrorCount += result.fixableErrorCount;
        fixableWarningCount += result.fixableWarningCount;

    }

    if (errorCount === 0 && warningCount === 0) {
        return "";
    }

    output += "\nFound:\n"
        + (errorCount > 0 ? `${colorize(`${errorCount} errors`, Colors.Red)} (${fixableErrorCount} possibly fixable with --fix option)\n` : "")
        + (warningCount > 0 ? `${colorize(`${warningCount} warnings`, Colors.Yellow)} (${fixableWarningCount} possibly fixable with --fix option)\n` : "");

    output += (errorCount > 0 ? "Please ensure to fix all errors before committing your code.\n\n" : "");

    return output;
};

const Colors = {
    Red: "\x1b[0;31m",
    Yellow: "\x1b[0;33m",
};

function colorize(text, color) {
    const colorOff = "\x1b[0m";
    return color + text + colorOff;
}
