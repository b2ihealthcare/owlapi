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
package org.semanticweb.owlapi.api.test.annotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.semanticweb.owlapi.api.test.baseclasses.AbstractRoundTrippingTestCase;
import org.semanticweb.owlapi.model.HasIRI;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLEntityRenamer;

@SuppressWarnings("javadoc")
public class RelativeIRIsRoundTripTestCase extends AbstractRoundTrippingTestCase {
    private final String ns = "";
    // "urn:test:ns#";
    private final OWLDataProperty d = df.getOWLDataProperty(IRI.create(ns + "d"));
    private final OWLObjectProperty o = df.getOWLObjectProperty(IRI.create(ns + "o"));
    private final OWLAnnotationProperty x = df.getOWLAnnotationProperty(IRI.create(ns + "X"));
    private final OWLAnnotationProperty y = df.getOWLAnnotationProperty(IRI.create(ns + "Y"));
    private final OWLAnnotation ann1 = df.getOWLAnnotation(x, df.getOWLLiteral("x"));
    private final OWLAnnotation ann2 = df.getOWLAnnotation(y, df.getOWLLiteral("y"));

    @Override
    protected OWLOntology createOntology() {
        OWLClassExpression c1 = df.getOWLDataAllValuesFrom(d, df.getBooleanOWLDatatype());
        OWLClassExpression c2 = df.getOWLObjectSomeValuesFrom(o, df.getOWLThing());
        OWLAxiom a = df.getOWLSubClassOfAxiom(c1, c2, new HashSet<>(Arrays.asList(ann1, ann2)));

        OWLOntology ont1 = getOWLOntology("");
        ont1.getOWLOntologyManager().addAxiom(ont1, a);
        return ont1;
    }

    @Override
    public boolean equal(OWLOntology ont1, OWLOntology ont2) {
        if (!ont2.containsDataPropertyInSignature(d.getIRI())) {
            OWLOntologyManager m2 = ont2.getOWLOntologyManager();
            OWLEntityRenamer renamer = new OWLEntityRenamer(m2, Collections.singleton(ont2));
            m2.applyChanges(renamer.changeIRI(relativise(d), d.getIRI()));
            m2.applyChanges(renamer.changeIRI(relativise(o), o.getIRI()));
            m2.applyChanges(renamer.changeIRI(relativise(x), x.getIRI()));
            m2.applyChanges(renamer.changeIRI(relativise(y), y.getIRI()));
        }
        return super.equal(ont1, ont2);
    }

    protected IRI relativise(HasIRI iri) {
        return IRI.create("http://www.semanticweb.org/owlapi/test/", iri.getIRI().toString());
    }
}
