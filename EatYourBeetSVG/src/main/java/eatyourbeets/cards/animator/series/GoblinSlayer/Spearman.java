package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.badlogic.gdx.graphics.Color;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.GenericEffects.GenericEffect_EnterStance;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;

public class Spearman extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Spearman.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage();
    static
    {
        DATA.AddPreview(new Witch(), true);
        DATA.AddPreview(new FakeAbstractCard(new Wound()), false);
    }

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Spearman()
    {
        super(DATA);

        Initialize(8, 0, 1);
        SetUpgrade(4, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SPEAR).SetVFXColor(Color.LIGHT_GRAY).SetSoundPitch(0.75f, 0.85f);
        GameActions.Bottom.GainAgility(magicNumber, true);
        GameActions.Bottom.GainForce(magicNumber, true);
        GameActions.Bottom.MakeCardInDrawPile(new Wound());

        if (choices.TryInitialize(this))
        {
            choices.AddEffect(new GenericEffect_EnterStance(ForceStance.STANCE_ID));
            choices.AddEffect(new GenericEffect_EnterStance(AgilityStance.STANCE_ID));
            choices.Initialize(new Witch());
            choices.AddEffect(new GenericEffect_EnterStance(IntellectStance.STANCE_ID));
            choices.AddEffect(new GenericEffect_EnterStance(NeutralStance.STANCE_ID));
            choices.Initialize(this);
        }

        if (CombatStats.CanActivateSemiLimited(cardID))
        {
            for (AbstractCard c : p.hand.group)
            {
                if (c.cardID.equals(Witch.DATA.ID) && CombatStats.TryActivateSemiLimited(cardID))
                {
                    choices.Select(1, m);
                    break;
                }
            }
        }
    }
}