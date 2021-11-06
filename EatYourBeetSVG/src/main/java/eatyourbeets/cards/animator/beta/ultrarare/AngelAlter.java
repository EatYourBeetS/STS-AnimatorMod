package eatyourbeets.cards.animator.beta.ultrarare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class AngelAlter extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(AngelAlter.class).SetAttack(0, CardRarity.SPECIAL, EYBAttackType.Piercing).SetColor(CardColor.COLORLESS);

    public AngelAlter()
    {
        super(DATA);

        Initialize(10, 0, 1, 0);


        SetSynergy(Synergies.AngelBeats);
        AfterLifeMod.Add(this);
    }

    @Override
    protected void OnUpgrade()
    {

    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);

        GameActions.Bottom.GainForce(magicNumber, true);
        GameActions.Bottom.GainAgility(magicNumber, true);

        GameActions.Bottom.MakeCardInDrawPile(makeStatEquivalentCopy());
        GameActions.Bottom.MakeCardInDiscardPile(makeStatEquivalentCopy());
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return super.cardPlayable(m) && !isInAutoplay && CombatStats.ControlPile.Contains(this) && AfterLifeMod.IsAdded(this);
    }
}