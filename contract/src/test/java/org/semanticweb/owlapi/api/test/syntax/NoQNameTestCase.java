/* This file is part of the OWL API.
 * The contents of this file are subject to the LGPL License, Version 3.0.
 * Copyright 2014, The University of Manchester
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0 in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. */
package org.semanticweb.owlapi.api.test.syntax;

import static org.junit.Assert.fail;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.IRI;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.NamedIndividual;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectProperty;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.ObjectPropertyAssertion;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import org.junit.Test;
import org.semanticweb.owlapi.api.test.baseclasses.AbstractAxiomsRoundTrippingTestCase;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.rdf.rdfxml.renderer.IllegalElementNameException;

/**
 * @author Matthew Horridge, The University of Manchester, Information Management Group
 * @since 3.0.0
 */
public class NoQNameTestCase extends AbstractAxiomsRoundTrippingTestCase {

    @Nonnull
    @Override
    protected Set<? extends OWLAxiom> createAxioms() {
        Set<OWLAxiom> axioms = new HashSet<>();
        OWLNamedIndividual indA =
            NamedIndividual(IRI("http://example.com/place/112013e2-df48-4a34-8a9d-99ef572a395A"));
        OWLNamedIndividual indB =
            NamedIndividual(IRI("http://example.com/place/112013e2-df48-4a34-8a9d-99ef572a395B"));
        OWLObjectProperty property = ObjectProperty(IRI("http://example.com/place/123"));
        axioms.add(ObjectPropertyAssertion(property, indA, indB));
        return axioms;
    }

    @Override
    @Test
    public void testRDFXML() throws Exception {
        try {
            super.testRDFXML();
            fail("Expected an exception specifying that a QName could not be generated");
        } catch (OWLOntologyStorageException e) {
            if (!(e.getCause() instanceof IllegalElementNameException)) {
                throw e;
            }
        }
    }

    @Override
    public void testRioRDFXML() throws Exception {
        try {
            super.testRioRDFXML();
            fail("Expected an exception specifying that a QName could not be generated");
        } catch (OWLOntologyStorageException e) {
            if (!(e.getCause() instanceof IOException)) {
                throw e;
            }
        }
    }

    @Override
    public void roundTripRDFXMLAndFunctionalShouldBeSame() {
        // Test meaningless in this case, as the RDF/XML serialization does not
        // exist
    }
}
