package eatyourbeets.cards.animator.colorless.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.TanyaDegurechaff_Type95;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class TanyaDegurechaff extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TanyaDegurechaff.class)
            .SetAttack(2, CardRarity.RARE, EYBAttackType.Ranged)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.YoujoSenki);
    static
    {
        DATA.AddPreview(new TanyaDegurechaff_Type95(), false);
    }

    public TanyaDegurechaff()
    {
        super(DATA);

        Initialize(4, 6, 1);
        SetUpgrade(2, 2);

        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        GameUtilities.IncreaseMagicNumber(this, player.hand.getSkills().size(), true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DiscardFromHand(name, 999, true)
        .SetOptions(false, true, true)
        .SetFilter(c -> c.type == CardType.SKILL)
        .AddCallback(m, (enemy, cards) ->
        {
            for (int i = 0; i < (cards.size() + baseMagicNumber); i++)
            {
                GameActions.Bottom.SFX("ATTACK_FIRE");
                GameActions.Bottom.DealDamage(this, enemy, AttackEffects.NONE);
            }
        });
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle && CombatStats.TryActivateLimited(cardID))
        {
            GameEffects.List.ShowCopy(this);
            GameActions.Bottom.MakeCardInDrawPile(new TanyaDegurechaff_Type95());
        }
    }
}