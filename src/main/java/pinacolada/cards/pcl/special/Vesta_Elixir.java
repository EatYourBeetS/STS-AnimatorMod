package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.GenericEffect;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect_GainOrbSlots;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect_GainTempHP;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class Vesta_Elixir extends PCLCard
{
    public static final PCLCardData DATA = Register(Vesta_Elixir.class)
            .SetSkill(0, CardRarity.SPECIAL, PCLCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.TenseiSlime);
    public static final int MAX_GROUP_SIZE = 3;
    public final ArrayList<GenericEffect> effects = new ArrayList<>();

    public Vesta_Elixir()
    {
        super(DATA);

        Initialize(0, 0, 3, 2);
        SetUpgrade(0, 0, 1, 1);

        SetAffinity_Star(1);
        SetPurge(true);
    }

    public Vesta_Elixir(ArrayList<GenericEffect> effects)
    {
        this();

        ApplyEffects(effects);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        Vesta_Elixir other = (Vesta_Elixir) super.makeStatEquivalentCopy();

        other.ApplyEffects(effects);

        return other;
    }

    @Override
    public boolean canUpgrade()
    {
        return effects.isEmpty();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (GenericEffect effect : effects)
        {
            effect.Use(this, p, m);
        }
    }

    public void ResearchEffects() {
        effects.clear();
        final RandomizedList<GenericEffect> possibleEffects = new RandomizedList<>();
        for (PCLAffinity af : PCLAffinity.Extended()) {
            possibleEffects.Add(GenericEffect.GainAffinityPower(magicNumber, af));
        }
        possibleEffects.Add(GenericEffect.Gain(secondaryValue, PCLPowerHelper.Inspiration));
        possibleEffects.Add(GenericEffect.Gain(magicNumber, PCLPowerHelper.Malleable));
        possibleEffects.Add(GenericEffect.Gain(secondaryValue, PCLPowerHelper.Metallicize));
        possibleEffects.Add(GenericEffect.Gain(secondaryValue, PCLPowerHelper.Energized));
        possibleEffects.Add(new GenericEffect_GainTempHP(magicNumber * 2));
        possibleEffects.Add(new GenericEffect_GainOrbSlots(secondaryValue));

        ChooseEffect(possibleEffects);
    }

    public void ChooseEffect(RandomizedList<GenericEffect> possibleEffects) {
        if (effects.size() >= MAX_GROUP_SIZE) {
            return;
        }
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        while (group.size() < MAX_GROUP_SIZE && possibleEffects.Size() > 0) {
            Vesta_Elixir other = (Vesta_Elixir) this.makeStatEquivalentCopy();
            other.ApplyEffect(possibleEffects.Retrieve(rng, true));
            group.addToTop(other);
        }

        PCLActions.Top.SelectFromPile(name, 1, group)
                .SetOptions(false, false)
                .AddCallback(c -> {
                    if (c.size() > 0) {
                        Vesta_Elixir elixir = PCLJUtils.SafeCast(c.get(0), Vesta_Elixir.class);
                        if (elixir != null) {
                            ApplyEffects(elixir.effects);
                            ChooseEffect(possibleEffects);
                        }
                    }
                });
    }

    protected void ApplyEffect(GenericEffect effect)
    {
        this.effects.add(effect);
        UpdateDescription();
    }

    protected void ApplyEffects(ArrayList<GenericEffect> effects)
    {
        this.effects.clear();
        this.effects.addAll(effects);
        UpdateDescription();
    }

    protected void UpdateDescription() {
        this.cardText.OverrideDescription(GenericEffect.JoinEffectTexts(effects), true);
    }
}