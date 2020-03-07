package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;

public class OrigamiTobiichi extends AnimatorCard {
    public static final EYBCardData DATA = Register(OrigamiTobiichi.class).SetPower(3, CardRarity.RARE);

    public OrigamiTobiichi() {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetSynergy(Synergies.DateALive);
    }



    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i=0; i<magicNumber; i++)
        {
            GameActions.Bottom.ChannelRandomOrb(true);
        }

        GameActions.Bottom.StackPower(new OrigamiTobiichiPower(p));
    }

    public static class OrigamiTobiichiPower extends AnimatorPower {
        private static final int SUPPORT_DAMAGE_AMOUNT = 1;
        private static final int SUPPORT_DAMAGE_LIMIT = 20;

        public OrigamiTobiichiPower(AbstractPlayer owner) {
            super(owner, OrigamiTobiichi.DATA);

            updateDescription();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, SUPPORT_DAMAGE_AMOUNT, SUPPORT_DAMAGE_LIMIT);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer) {
            if (isPlayer)
            {
                AbstractPlayer player = AbstractDungeon.player;

                GameActions.Bottom.StackPower(new SupportDamagePower(player, player.orbs.size()));

                int supportDamageCount = player.getPower(SupportDamagePower.POWER_ID).amount;

                if (supportDamageCount >- 20)
                {
                    
                }
            }
        }
    }
}