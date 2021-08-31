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
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class TanyaDegurechaff extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TanyaDegurechaff.class)
            .SetAttack(1, CardRarity.RARE, EYBAttackType.Ranged)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.YoujoSenki)
            .PostInitialize(data -> data.AddPreview(new TanyaDegurechaff_Type95(), false));

    public TanyaDegurechaff()
    {
        super(DATA);

        Initialize(5, 3, 2, 2);
        SetUpgrade(3, 1, 0, 0);

        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT).SetSoundPitch(0.75f, 0.8f);
        GameActions.Bottom.DiscardFromPile(name, secondaryValue, p.drawPile)
        .ShowEffect(true, true)
        .SetFilter(c -> c.type == CardType.SKILL)
        .SetOptions(CardSelection.Top, true);
        GameActions.Bottom.Draw(magicNumber);
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