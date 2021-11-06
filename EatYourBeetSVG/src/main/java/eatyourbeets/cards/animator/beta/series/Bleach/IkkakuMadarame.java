package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.IkkakuBankai;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class IkkakuMadarame extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IkkakuMadarame.class).SetAttack(2, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL);
    static
    {
        DATA.AddPreview(new ZarakiKenpachi(), false);
        DATA.AddPreview(new IkkakuBankai(), false);
    }

    public IkkakuMadarame()
    {
        super(DATA);

        Initialize(4, 0, 0, ForcePower.GetThreshold(2));
        SetUpgrade(3, 0, 0);


        SetSynergy(Synergies.Bleach);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);

        if (GameUtilities.GetPowerAmount(ZarakiKenpachi.DATA.ID + "Power") > 0)
        {
            GameActions.Bottom.StackPower(new IkkakuMadaramePower(player, 1));
        }

        GameActions.Bottom.Callback(card -> {
            if (ForcePower.GetCurrentLevel() > 2 || AgilityPower.GetCurrentLevel() > 2 )
            {
                GameActions.Bottom.MakeCardInDrawPile(new IkkakuBankai());
                GameActions.Last.ModifyAllInstances(uuid).AddCallback(GameActions.Bottom::Exhaust);
            }
        });
    }

    public static class IkkakuMadaramePower extends AnimatorPower
    {
        public IkkakuMadaramePower(AbstractPlayer owner, int amount)
        {
            super(owner, IkkakuMadarame.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            AgilityPower.StartOverrideDisable();
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            AgilityPower.StopOverrideDisable();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);
            RemovePower();
        }
    }
}