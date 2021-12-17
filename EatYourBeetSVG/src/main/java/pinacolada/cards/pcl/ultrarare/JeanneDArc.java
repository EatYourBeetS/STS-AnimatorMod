package pinacolada.cards.pcl.ultrarare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_UltraRare;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class JeanneDArc extends PCLCard_UltraRare
{
    public static final PCLCardData DATA = Register(JeanneDArc.class)
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
        int existingAmount = PCLGameUtilities.GetPowerAmount(player, InvinciblePower.POWER_ID);
        if (existingAmount > 0) {
            PCLActions.Bottom.RemovePower(player,player,InvinciblePower.POWER_ID);
            PCLActions.Last.ApplyPower(new InvinciblePower(player, existingAmount / 2));
        }
        else {
            PCLActions.Bottom.ApplyPower(new InvinciblePower(player, player.maxHealth / 5));
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            PCLGameEffects.List.ShowCopy(this);
            PCLActions.Bottom.LoseHP(player.currentHealth / 2, AbstractGameAction.AttackEffect.NONE);
            PCLActions.Last.PlayCard(this, null);
        }
    }
}