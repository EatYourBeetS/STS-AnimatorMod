package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Excalibur;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;

public class OrigamiTobiichi extends AnimatorCard {
    public static final EYBCardData DATA = Register(OrigamiTobiichi.class).SetPower(2, CardRarity.UNCOMMON);
    static
    {
        DATA.AddPreview(new InverseOrigami(), false);
    }

    public OrigamiTobiichi() {
        super(DATA);

        Initialize(0, 0);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded)
        {
            GameActions.Bottom.ChannelRandomOrb(true);
        }

        GameActions.Bottom.StackPower(new OrigamiTobiichiPower(p, upgraded));
    }

    public static class OrigamiTobiichiPower extends AnimatorPower {
        private static final int SUPPORT_DAMAGE_AMOUNT = 1;
        private static final int SUPPORT_DAMAGE_LIMIT = 20;
        private final boolean upgraded;

        public OrigamiTobiichiPower(AbstractPlayer owner, boolean upgraded) {
            super(owner, OrigamiTobiichi.DATA);

            this.amount += 1;
            this.upgraded = upgraded;

            updateDescription();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, SUPPORT_DAMAGE_AMOUNT * amount, SUPPORT_DAMAGE_LIMIT);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer) {
            if (isPlayer)
            {
                AbstractPlayer player = AbstractDungeon.player;

                GameActions.Bottom.StackPower(new SupportDamagePower(player, player.orbs.size()*amount));

                int supportDamageCount = player.getPower(SupportDamagePower.POWER_ID).amount;

                if (supportDamageCount >- 25)
                {
                    GameActions.Bottom.MakeCardInDrawPile(new InverseOrigami()).SetOptions(this.upgraded, false);
                    this.amount -= 1;
                }
            }
        }
    }
}