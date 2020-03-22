package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.OnApplyPowerSubscriber;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.powers.common.TemporaryRetainPower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class OrikoMikuni extends AnimatorCard
{
    public static final EYBCardData DATA = Register(OrikoMikuni.class).SetSkill(0, CardRarity.COMMON, EYBCardTarget.None);

    public OrikoMikuni()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 1);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Scry(magicNumber);

        if (!p.hand.isEmpty())
        {
            GameActions.Bottom.StackPower(new TemporaryRetainPower(p, secondaryValue));
        }

        boolean intGainedThisTurn = false;

        IntellectPower intellect = GameUtilities.GetPower(player, IntellectPower.class);
        if (intellect != null && intellect.GetCurrentLevel() > 1)
        {
            intGainedThisTurn = true;
        }

        if (HasSynergy() && intGainedThisTurn && EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.Draw(1);
        }
    }
}