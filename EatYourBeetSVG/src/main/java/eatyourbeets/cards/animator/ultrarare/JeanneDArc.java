package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class JeanneDArc extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(JeanneDArc.class)
            .SetPower(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Fate);

    public JeanneDArc()
    {
        super(DATA);

        Initialize(0, 0, 8);
        SetUpgrade(0, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Light(1, 0, 2);
        SetCostUpgrade(-1);

        SetLoyal(true);
    }



    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int existingAmount = GameUtilities.GetPowerAmount(player, InvinciblePower.POWER_ID);
        if (existingAmount > 0) {
            GameActions.Bottom.RemovePower(player,player,InvinciblePower.POWER_ID);
            GameActions.Last.ApplyPower(new InvinciblePower(player, existingAmount / 2));
        }
        else {
            GameActions.Bottom.ApplyPower(new InvinciblePower(player, player.maxHealth / 5));
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            GameEffects.List.ShowCopy(this);
            GameActions.Bottom.LoseHP(player.currentHealth / 2, AbstractGameAction.AttackEffect.NONE);
            GameActions.Last.PlayCard(this, null);
        }
    }
}