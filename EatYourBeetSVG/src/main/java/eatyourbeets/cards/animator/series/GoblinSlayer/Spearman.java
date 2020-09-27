package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
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
    public static final EYBCardData DATA = Register(Spearman.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Piercing);
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
        SetScaling(0, 1, 1);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
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

        if (!CombatStats.HasActivatedSemiLimited(cardID))
        {
            for (AbstractCard c : p.hand.group)
            {
                if (c.cardID.equals(Witch.DATA.ID) && CombatStats.TryActivateSemiLimited(cardID))
                {
                    CardGroup group = choices.Build(true);
                    group.removeCard(player.stance != null ? player.stance.ID : NeutralStance.STANCE_ID);
                    choices.Select(GameActions.Bottom, group,1, m).IsCancellable(true);
                    break;
                }
            }
        }
    }
}