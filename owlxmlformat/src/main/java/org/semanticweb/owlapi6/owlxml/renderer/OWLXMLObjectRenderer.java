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
package org.semanticweb.owlapi6.owlxml.renderer;

import static org.semanticweb.owlapi6.utilities.OWLAPIPreconditions.checkNotNull;
import static org.semanticweb.owlapi6.utilities.OWLAPIStreamUtils.asUnorderedSet;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.ANNOTATION;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.ANNOTATION_ASSERTION;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.ANNOTATION_PROPERTY;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.ANNOTATION_PROPERTY_DOMAIN;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.ANNOTATION_PROPERTY_RANGE;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.ANONYMOUS_INDIVIDUAL;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.ASYMMETRIC_OBJECT_PROPERTY;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.BODY;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.BUILT_IN_ATOM;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.CLASS;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.CLASS_ASSERTION;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.CLASS_ATOM;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DATATYPE;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DATATYPE_DEFINITION;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DATATYPE_RESTRICTION;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DATA_ALL_VALUES_FROM;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DATA_COMPLEMENT_OF;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DATA_EXACT_CARDINALITY;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DATA_HAS_VALUE;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DATA_INTERSECTION_OF;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DATA_MAX_CARDINALITY;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DATA_MIN_CARDINALITY;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DATA_ONE_OF;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DATA_PROPERTY;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DATA_PROPERTY_ASSERTION;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DATA_PROPERTY_ATOM;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DATA_PROPERTY_DOMAIN;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DATA_PROPERTY_RANGE;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DATA_RANGE_ATOM;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DATA_SOME_VALUES_FROM;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DATA_UNION_OF;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DECLARATION;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DIFFERENT_INDIVIDUALS;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DIFFERENT_INDIVIDUALS_ATOM;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DISJOINT_CLASSES;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DISJOINT_DATA_PROPERTIES;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DISJOINT_OBJECT_PROPERTIES;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DISJOINT_UNION;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.DL_SAFE_RULE;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.EQUIVALENT_CLASSES;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.EQUIVALENT_DATA_PROPERTIES;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.EQUIVALENT_OBJECT_PROPERTIES;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.FACET_RESTRICTION;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.FUNCTIONAL_DATA_PROPERTY;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.FUNCTIONAL_OBJECT_PROPERTY;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.HAS_KEY;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.HEAD;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.IMPORT;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.INVERSE_FUNCTIONAL_OBJECT_PROPERTY;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.INVERSE_OBJECT_PROPERTIES;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.IRREFLEXIVE_OBJECT_PROPERTY;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.LITERAL;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.NAMED_INDIVIDUAL;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.NEGATIVE_DATA_PROPERTY_ASSERTION;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.NEGATIVE_OBJECT_PROPERTY_ASSERTION;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.OBJECT_ALL_VALUES_FROM;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.OBJECT_COMPLEMENT_OF;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.OBJECT_EXACT_CARDINALITY;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.OBJECT_HAS_SELF;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.OBJECT_HAS_VALUE;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.OBJECT_INTERSECTION_OF;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.OBJECT_INVERSE_OF;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.OBJECT_MAX_CARDINALITY;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.OBJECT_MIN_CARDINALITY;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.OBJECT_ONE_OF;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.OBJECT_PROPERTY;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.OBJECT_PROPERTY_ASSERTION;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.OBJECT_PROPERTY_ATOM;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.OBJECT_PROPERTY_CHAIN;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.OBJECT_PROPERTY_DOMAIN;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.OBJECT_PROPERTY_RANGE;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.OBJECT_SOME_VALUES_FROM;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.OBJECT_UNION_OF;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.REFLEXIVE_OBJECT_PROPERTY;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.SAME_INDIVIDUAL;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.SAME_INDIVIDUAL_ATOM;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.SUB_ANNOTATION_PROPERTY_OF;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.SUB_CLASS_OF;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.SUB_DATA_PROPERTY_OF;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.SUB_OBJECT_PROPERTY_OF;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.SYMMETRIC_OBJECT_PROPERTY;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.TRANSITIVE_OBJECT_PROPERTY;
import static org.semanticweb.owlapi6.vocab.OWLXMLVocabulary.VARIABLE;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi6.model.AxiomType;
import org.semanticweb.owlapi6.model.HasAnnotations;
import org.semanticweb.owlapi6.model.HasIRI;
import org.semanticweb.owlapi6.model.IRI;
import org.semanticweb.owlapi6.model.OWLAnnotation;
import org.semanticweb.owlapi6.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi6.model.OWLAnnotationProperty;
import org.semanticweb.owlapi6.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi6.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi6.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi6.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi6.model.OWLAxiom;
import org.semanticweb.owlapi6.model.OWLCardinalityRestriction;
import org.semanticweb.owlapi6.model.OWLClass;
import org.semanticweb.owlapi6.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi6.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi6.model.OWLDataComplementOf;
import org.semanticweb.owlapi6.model.OWLDataExactCardinality;
import org.semanticweb.owlapi6.model.OWLDataFactory;
import org.semanticweb.owlapi6.model.OWLDataHasValue;
import org.semanticweb.owlapi6.model.OWLDataIntersectionOf;
import org.semanticweb.owlapi6.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi6.model.OWLDataMinCardinality;
import org.semanticweb.owlapi6.model.OWLDataOneOf;
import org.semanticweb.owlapi6.model.OWLDataProperty;
import org.semanticweb.owlapi6.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi6.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi6.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi6.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi6.model.OWLDataUnionOf;
import org.semanticweb.owlapi6.model.OWLDatatype;
import org.semanticweb.owlapi6.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi6.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi6.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi6.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi6.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi6.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi6.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi6.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi6.model.OWLEntity;
import org.semanticweb.owlapi6.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi6.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi6.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi6.model.OWLFacetRestriction;
import org.semanticweb.owlapi6.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi6.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi6.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi6.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi6.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi6.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi6.model.OWLLiteral;
import org.semanticweb.owlapi6.model.OWLNamedIndividual;
import org.semanticweb.owlapi6.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi6.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi6.model.OWLObject;
import org.semanticweb.owlapi6.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi6.model.OWLObjectComplementOf;
import org.semanticweb.owlapi6.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi6.model.OWLObjectHasSelf;
import org.semanticweb.owlapi6.model.OWLObjectHasValue;
import org.semanticweb.owlapi6.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi6.model.OWLObjectInverseOf;
import org.semanticweb.owlapi6.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi6.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi6.model.OWLObjectOneOf;
import org.semanticweb.owlapi6.model.OWLObjectProperty;
import org.semanticweb.owlapi6.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi6.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi6.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi6.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi6.model.OWLObjectUnionOf;
import org.semanticweb.owlapi6.model.OWLObjectVisitor;
import org.semanticweb.owlapi6.model.OWLOntology;
import org.semanticweb.owlapi6.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi6.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi6.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi6.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi6.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi6.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi6.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi6.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi6.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi6.model.SWRLBuiltInAtom;
import org.semanticweb.owlapi6.model.SWRLClassAtom;
import org.semanticweb.owlapi6.model.SWRLDataPropertyAtom;
import org.semanticweb.owlapi6.model.SWRLDataRangeAtom;
import org.semanticweb.owlapi6.model.SWRLDifferentIndividualsAtom;
import org.semanticweb.owlapi6.model.SWRLIndividualArgument;
import org.semanticweb.owlapi6.model.SWRLLiteralArgument;
import org.semanticweb.owlapi6.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi6.model.SWRLRule;
import org.semanticweb.owlapi6.model.SWRLSameIndividualAtom;
import org.semanticweb.owlapi6.model.SWRLVariable;
import org.semanticweb.owlapi6.model.parameters.Imports;
import org.semanticweb.owlapi6.vocab.OWL2Datatype;
import org.semanticweb.owlapi6.vocab.OWLXMLVocabulary;

/**
 * @author Matthew Horridge, The University Of Manchester, Bio-Health
 *         Informatics Group
 * @since 2.0.0
 */
public class OWLXMLObjectRenderer implements OWLObjectVisitor {

    private final OWLXMLWriter writer;
    private OWLDataFactory df;

    /**
     * @param writer
     *        writer
     * @param df
     *        data factory
     */
    public OWLXMLObjectRenderer(OWLXMLWriter writer, OWLDataFactory df) {
        this.writer = checkNotNull(writer, "writer cannot be null");
        this.df = df;
    }

    @Override
    public void visit(OWLOntology ontology) {
        checkNotNull(ontology, "ontology cannot be null");
        ontology.importsDeclarations().sorted().forEach(decl -> {
            writer.writeStartElement(IMPORT);
            writer.writeTextContent(decl.getIRI().toString());
            writer.writeEndElement();
        });
        ontology.annotationsAsList().forEach(this::accept);
        // treat declarations separately from other axioms
        Set<OWLEntity> declared = asUnorderedSet(ontology.unsortedSignature());
        ontology.axioms(AxiomType.DECLARATION).sorted().forEach(ax -> {
            ax.accept(this);
            declared.remove(ax.getEntity());
        });
        // any undeclared entities?
        if (!declared.isEmpty()) {
            boolean addMissing = ontology.getOWLOntologyManager().getOntologyConfigurator().shouldAddMissingTypes();
            if (addMissing) {
                Collection<IRI> illegalPunnings = ontology.determineIllegalPunnings(addMissing);
                for (OWLEntity e : declared) {
                    if (!e.isBuiltIn() && !illegalPunnings.contains(e.getIRI())
                        && !ontology.isDeclared(e, Imports.INCLUDED)) {
                        ontology.getOWLOntologyManager().getOWLDataFactory().getOWLDeclarationAxiom(e).accept(this);
                    }
                }
            }
        }
        AxiomType.skipDeclarations().flatMap(ontology::axioms).distinct().sorted().forEach(this::accept);
    }

    private void accept(OWLObject o) {
        o.accept(this);
    }

    private void axiom(OWLXMLVocabulary v, OWLAxiom ax, OWLObject... objects) {
        writer.writeStartElement(v);
        ax.annotationsAsList().forEach(this::accept);
        for (OWLObject o : objects) {
            accept(o);
        }
        writer.writeEndElement();
    }

    private void ann(OWLXMLVocabulary v, HasAnnotations ax, OWLObject... objects) {
        writer.writeStartElement(v);
        ax.annotationsAsList().forEach(this::accept);
        for (OWLObject o : objects) {
            accept(o);
        }
        writer.writeEndElement();
    }

    private void axiom(OWLXMLVocabulary v, OWLObject o) {
        writer.writeStartElement(v);
        accept(o);
        writer.writeEndElement();
    }

    private void axiom(OWLXMLVocabulary v, OWLObject... objects) {
        writer.writeStartElement(v);
        for (OWLObject o : objects) {
            accept(o);
        }
        writer.writeEndElement();
    }

    private void axiom(OWLXMLVocabulary v, List<? extends OWLObject> o) {
        writer.writeStartElement(v);
        o.forEach(this::accept);
        writer.writeEndElement();
    }

    private void axiom(OWLXMLVocabulary v, OWLAxiom ax, List<? extends OWLObject> o) {
        writer.writeStartElement(v);
        ax.annotationsAsList().forEach(this::accept);
        o.forEach(this::accept);
        writer.writeEndElement();
    }

    private void axiom(OWLXMLVocabulary v, OWLAxiom ax, OWLObject o, List<? extends OWLObject> l) {
        writer.writeStartElement(v);
        ax.annotationsAsList().forEach(this::accept);
        accept(o);
        l.forEach(this::accept);
        writer.writeEndElement();
    }

    private void axiom(OWLXMLVocabulary v, OWLObject o, List<? extends OWLObject> l) {
        writer.writeStartElement(v);
        accept(o);
        l.forEach(this::accept);
        writer.writeEndElement();
    }

    @Override
    public void visit(IRI iri) {
        checkNotNull(iri, "iri cannot be null");
        writer.writeIRIElement(iri);
    }

    @Override
    public void visit(OWLAnonymousIndividual individual) {
        writer.writeStartElement(ANONYMOUS_INDIVIDUAL);
        writer.writeNodeIDAttribute(individual.getID());
        writer.writeEndElement();
    }

    @Override
    public void visit(OWLAsymmetricObjectPropertyAxiom axiom) {
        axiom(ASYMMETRIC_OBJECT_PROPERTY, axiom, axiom.getProperty());
    }

    @Override
    public void visit(OWLClassAssertionAxiom axiom) {
        axiom(CLASS_ASSERTION, axiom, axiom.getClassExpression(), axiom.getIndividual());
    }

    @Override
    public void visit(OWLDataPropertyAssertionAxiom axiom) {
        axiom(DATA_PROPERTY_ASSERTION, axiom, axiom.getProperty(), axiom.getSubject(), axiom.getObject());
    }

    @Override
    public void visit(OWLDataPropertyDomainAxiom axiom) {
        axiom(DATA_PROPERTY_DOMAIN, axiom, axiom.getProperty(), axiom.getDomain());
    }

    @Override
    public void visit(OWLDataPropertyRangeAxiom axiom) {
        axiom(DATA_PROPERTY_RANGE, axiom, axiom.getProperty(), axiom.getRange());
    }

    @Override
    public void visit(OWLSubDataPropertyOfAxiom axiom) {
        axiom(SUB_DATA_PROPERTY_OF, axiom, axiom.getSubProperty(), axiom.getSuperProperty());
    }

    @Override
    public void visit(OWLDeclarationAxiom axiom) {
        axiom(DECLARATION, axiom, axiom.getEntity());
    }

    @Override
    public void visit(OWLDifferentIndividualsAxiom axiom) {
        axiom(DIFFERENT_INDIVIDUALS, axiom, axiom.getOperandsAsList());
    }

    @Override
    public void visit(OWLDisjointClassesAxiom axiom) {
        axiom(DISJOINT_CLASSES, axiom, axiom.getOperandsAsList());
    }

    @Override
    public void visit(OWLDisjointDataPropertiesAxiom axiom) {
        axiom(DISJOINT_DATA_PROPERTIES, axiom, axiom.getOperandsAsList());
    }

    @Override
    public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
        axiom(DISJOINT_OBJECT_PROPERTIES, axiom, axiom.getOperandsAsList());
    }

    @Override
    public void visit(OWLDisjointUnionAxiom axiom) {
        axiom(DISJOINT_UNION, axiom, axiom.getOWLClass(), axiom.getOperandsAsList());
    }

    @Override
    public void visit(OWLAnnotationAssertionAxiom axiom) {
        axiom(ANNOTATION_ASSERTION, axiom, axiom.getProperty(), axiom.getSubject(), axiom.getValue());
    }

    @Override
    public void visit(OWLEquivalentClassesAxiom axiom) {
        axiom(EQUIVALENT_CLASSES, axiom, axiom.getOperandsAsList());
    }

    @Override
    public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
        axiom(EQUIVALENT_DATA_PROPERTIES, axiom, axiom.getOperandsAsList());
    }

    @Override
    public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
        axiom(EQUIVALENT_OBJECT_PROPERTIES, axiom, axiom.getOperandsAsList());
    }

    @Override
    public void visit(OWLFunctionalDataPropertyAxiom axiom) {
        axiom(FUNCTIONAL_DATA_PROPERTY, axiom, axiom.getProperty());
    }

    @Override
    public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
        axiom(FUNCTIONAL_OBJECT_PROPERTY, axiom, axiom.getProperty());
    }

    @Override
    public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
        axiom(INVERSE_FUNCTIONAL_OBJECT_PROPERTY, axiom, axiom.getProperty());
    }

    @Override
    public void visit(OWLInverseObjectPropertiesAxiom axiom) {
        axiom(INVERSE_OBJECT_PROPERTIES, axiom, axiom.getFirstProperty(), axiom.getSecondProperty());
    }

    @Override
    public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
        axiom(IRREFLEXIVE_OBJECT_PROPERTY, axiom, axiom.getProperty());
    }

    @Override
    public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
        axiom(NEGATIVE_DATA_PROPERTY_ASSERTION, axiom, axiom.getProperty(), axiom.getSubject(), axiom.getObject());
    }

    @Override
    public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
        axiom(NEGATIVE_OBJECT_PROPERTY_ASSERTION, axiom, axiom.getProperty(), axiom.getSubject(), axiom.getObject());
    }

    @Override
    public void visit(OWLObjectPropertyAssertionAxiom axiom) {
        axiom(OBJECT_PROPERTY_ASSERTION, axiom, axiom.getProperty(), axiom.getSubject(), axiom.getObject());
    }

    @Override
    public void visit(OWLSubPropertyChainOfAxiom axiom) {
        writer.writeStartElement(SUB_OBJECT_PROPERTY_OF);
        axiom.annotationsAsList().forEach(this::accept);
        axiom(OBJECT_PROPERTY_CHAIN, axiom.getPropertyChain());
        accept(axiom.getSuperProperty());
        writer.writeEndElement();
    }

    @Override
    public void visit(OWLObjectPropertyDomainAxiom axiom) {
        axiom(OBJECT_PROPERTY_DOMAIN, axiom, axiom.getProperty(), axiom.getDomain());
    }

    @Override
    public void visit(OWLObjectPropertyRangeAxiom axiom) {
        axiom(OBJECT_PROPERTY_RANGE, axiom, axiom.getProperty(), axiom.getRange());
    }

    @Override
    public void visit(OWLSubObjectPropertyOfAxiom axiom) {
        axiom(SUB_OBJECT_PROPERTY_OF, axiom, axiom.getSubProperty(), axiom.getSuperProperty());
    }

    @Override
    public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
        axiom(REFLEXIVE_OBJECT_PROPERTY, axiom, axiom.getProperty());
    }

    @Override
    public void visit(OWLSameIndividualAxiom axiom) {
        axiom(SAME_INDIVIDUAL, axiom, axiom.getOperandsAsList());
    }

    @Override
    public void visit(OWLSubClassOfAxiom axiom) {
        axiom(SUB_CLASS_OF, axiom, axiom.getSubClass(), axiom.getSuperClass());
    }

    @Override
    public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
        axiom(SYMMETRIC_OBJECT_PROPERTY, axiom, axiom.getProperty());
    }

    @Override
    public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
        axiom(TRANSITIVE_OBJECT_PROPERTY, axiom, axiom.getProperty());
    }

    private void iri(OWLXMLVocabulary v, HasIRI o) {
        writer.writeStartElement(v);
        writer.writeIRIAttribute(o.getIRI());
        writer.writeEndElement();
    }

    @Override
    public void visit(OWLClass ce) {
        iri(CLASS, ce);
    }

    @Override
    public void visit(OWLDataAllValuesFrom ce) {
        axiom(DATA_ALL_VALUES_FROM, ce.getProperty(), ce.getFiller());
    }

    @Override
    public void visit(OWLDataExactCardinality ce) {
        card(DATA_EXACT_CARDINALITY, ce);
    }

    protected void card(OWLXMLVocabulary v, OWLCardinalityRestriction<? extends OWLObject> ce) {
        writer.writeStartElement(v);
        writer.writeCardinalityAttribute(ce.getCardinality());
        accept(ce.getProperty());
        if (ce.isQualified()) {
            accept(ce.getFiller());
        }
        writer.writeEndElement();
    }

    @Override
    public void visit(OWLDataMaxCardinality ce) {
        card(DATA_MAX_CARDINALITY, ce);
    }

    @Override
    public void visit(OWLDataMinCardinality ce) {
        card(DATA_MIN_CARDINALITY, ce);
    }

    @Override
    public void visit(OWLDataSomeValuesFrom ce) {
        writer.writeStartElement(DATA_SOME_VALUES_FROM);
        accept(ce.getProperty());
        accept(ce.getFiller());
        writer.writeEndElement();
    }

    @Override
    public void visit(OWLDataHasValue ce) {
        writer.writeStartElement(DATA_HAS_VALUE);
        accept(ce.getProperty());
        accept(ce.getFiller());
        writer.writeEndElement();
    }

    @Override
    public void visit(OWLObjectAllValuesFrom ce) {
        writer.writeStartElement(OBJECT_ALL_VALUES_FROM);
        accept(ce.getProperty());
        accept(ce.getFiller());
        writer.writeEndElement();
    }

    @Override
    public void visit(OWLObjectComplementOf ce) {
        axiom(OBJECT_COMPLEMENT_OF, ce.getOperand());
    }

    @Override
    public void visit(OWLObjectExactCardinality ce) {
        card(OBJECT_EXACT_CARDINALITY, ce);
    }

    @Override
    public void visit(OWLObjectIntersectionOf ce) {
        axiom(OBJECT_INTERSECTION_OF, ce.getOperandsAsList());
    }

    @Override
    public void visit(OWLObjectMaxCardinality ce) {
        card(OBJECT_MAX_CARDINALITY, ce);
    }

    @Override
    public void visit(OWLObjectMinCardinality ce) {
        card(OBJECT_MIN_CARDINALITY, ce);
    }

    @Override
    public void visit(OWLObjectOneOf ce) {
        axiom(OBJECT_ONE_OF, ce.getOperandsAsList());
    }

    @Override
    public void visit(OWLObjectHasSelf ce) {
        axiom(OBJECT_HAS_SELF, ce.getProperty());
    }

    @Override
    public void visit(OWLObjectSomeValuesFrom ce) {
        axiom(OBJECT_SOME_VALUES_FROM, ce.getProperty(), ce.getFiller());
    }

    @Override
    public void visit(OWLObjectUnionOf ce) {
        axiom(OBJECT_UNION_OF, ce.getOperandsAsList());
    }

    @Override
    public void visit(OWLObjectHasValue ce) {
        axiom(OBJECT_HAS_VALUE, ce.getProperty(), ce.getFiller());
    }

    @Override
    public void visit(OWLDataComplementOf node) {
        axiom(DATA_COMPLEMENT_OF, node.getDataRange());
    }

    @Override
    public void visit(OWLDataOneOf node) {
        axiom(DATA_ONE_OF, node.getOperandsAsList());
    }

    @Override
    public void visit(OWLDatatype node) {
        iri(DATATYPE, node);
    }

    @Override
    public void visit(OWLDatatypeRestriction node) {
        axiom(DATATYPE_RESTRICTION, node.getDatatype(), node.facetRestrictionsAsList());
    }

    @Override
    public void visit(OWLFacetRestriction node) {
        writer.writeStartElement(FACET_RESTRICTION);
        writer.writeFacetAttribute(node.getFacet());
        accept(node.getFacetValue());
        writer.writeEndElement();
    }

    @Override
    public void visit(OWLLiteral node) {
        writer.writeStartElement(LITERAL);
        if (node.hasLang()) {
            writer.writeLangAttribute(node.getLang());
        } else if (!node.isRDFPlainLiteral() && !OWL2Datatype.XSD_STRING.getIRI().equals(node.getDatatype().getIRI())) {
            writer.writeDatatypeAttribute(node.getDatatype());
        }
        writer.writeTextContent(node.getLiteral());
        writer.writeEndElement();
    }

    @Override
    public void visit(OWLDataProperty property) {
        iri(DATA_PROPERTY, property);
    }

    @Override
    public void visit(OWLObjectProperty property) {
        iri(OBJECT_PROPERTY, property);
    }

    @Override
    public void visit(OWLObjectInverseOf property) {
        axiom(OBJECT_INVERSE_OF, property.getInverse());
    }

    @Override
    public void visit(OWLNamedIndividual individual) {
        iri(NAMED_INDIVIDUAL, individual);
    }

    @Override
    public void visit(OWLHasKeyAxiom axiom) {
        writer.writeStartElement(HAS_KEY);
        axiom.annotationsAsList().forEach(this::accept);
        accept(axiom.getClassExpression());
        axiom.objectPropertyExpressions().forEach(this::accept);
        axiom.dataPropertyExpressions().forEach(this::accept);
        writer.writeEndElement();
    }

    @Override
    public void visit(OWLDataIntersectionOf node) {
        axiom(DATA_INTERSECTION_OF, node.getOperandsAsList());
    }

    @Override
    public void visit(OWLDataUnionOf node) {
        axiom(DATA_UNION_OF, node.getOperandsAsList());
    }

    @Override
    public void visit(OWLAnnotationProperty property) {
        iri(ANNOTATION_PROPERTY, property);
    }

    @Override
    public void visit(OWLAnnotation node) {
        ann(ANNOTATION, node, node.getProperty(), node.getValue());
    }

    @Override
    public void visit(OWLAnnotationPropertyDomainAxiom axiom) {
        axiom(ANNOTATION_PROPERTY_DOMAIN, axiom, axiom.getProperty(), axiom.getDomain());
    }

    @Override
    public void visit(OWLAnnotationPropertyRangeAxiom axiom) {
        axiom(ANNOTATION_PROPERTY_RANGE, axiom, axiom.getProperty(), axiom.getRange());
    }

    @Override
    public void visit(OWLSubAnnotationPropertyOfAxiom axiom) {
        axiom(SUB_ANNOTATION_PROPERTY_OF, axiom, axiom.getSubProperty(), axiom.getSuperProperty());
    }

    @Override
    public void visit(OWLDatatypeDefinitionAxiom axiom) {
        axiom(DATATYPE_DEFINITION, axiom, axiom.getDatatype(), axiom.getDataRange());
    }

    @Override
    public void visit(SWRLRule rule) {
        writer.writeStartElement(DL_SAFE_RULE);
        rule.annotationsAsList().forEach(this::accept);
        axiom(BODY, rule.bodyList());
        axiom(HEAD, rule.headList());
        writer.writeEndElement();
    }

    @Override
    public void visit(SWRLClassAtom node) {
        axiom(CLASS_ATOM, node.getPredicate(), node.getArgument());
    }

    @Override
    public void visit(SWRLDataRangeAtom node) {
        axiom(DATA_RANGE_ATOM, node.getPredicate(), node.getArgument());
    }

    @Override
    public void visit(SWRLObjectPropertyAtom node) {
        axiom(OBJECT_PROPERTY_ATOM, node.getPredicate(), node.getFirstArgument(), node.getSecondArgument());
    }

    @Override
    public void visit(SWRLDataPropertyAtom node) {
        axiom(DATA_PROPERTY_ATOM, node.getPredicate(), node.getFirstArgument(), node.getSecondArgument());
    }

    @Override
    public void visit(SWRLBuiltInAtom node) {
        writer.writeStartElement(BUILT_IN_ATOM);
        writer.writeIRIAttribute(node.getPredicate());
        node.getArguments().forEach(this::accept);
        writer.writeEndElement();
    }

    @Override
    public void visit(SWRLVariable node) {
        writer.writeStartElement(VARIABLE);
        if ("urn:swrl:var#".equals(node.getIRI().getNamespace()) || "urn:swrl#".equals(node.getIRI().getNamespace())) {
            writer.writeIRIAttribute(df.getIRI("urn:swrl:var#", node.getIRI().getFragment()));
        } else {
            writer.writeIRIAttribute(node.getIRI());
        }
        writer.writeEndElement();
    }

    @Override
    public void visit(SWRLIndividualArgument node) {
        accept(node.getIndividual());
    }

    @Override
    public void visit(SWRLLiteralArgument node) {
        accept(node.getLiteral());
    }

    @Override
    public void visit(SWRLDifferentIndividualsAtom node) {
        axiom(DIFFERENT_INDIVIDUALS_ATOM, node.getFirstArgument(), node.getSecondArgument());
    }

    @Override
    public void visit(SWRLSameIndividualAtom node) {
        axiom(SAME_INDIVIDUAL_ATOM, node.getFirstArgument(), node.getSecondArgument());
    }
}