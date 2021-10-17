package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import eatyourbeets.cards.animator.special.OrbCore;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Eve extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Eve.class)
            .SetPower(3, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    public Eve()
    {
        super(DATA);

        Initialize(0, 0, 1, 2 );
        SetUpgrade(0,0,1, 1);

        SetAffinity_Cyber(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Add(OrbCore.SelectCoreAction(name, 1)
        .AddCallback(orbCores ->
        {
            if (orbCores != null && orbCores.size() > 0)
            {
                for (AbstractCard c : orbCores)
                {
                    c.applyPowers();
                    c.use(player, null);
                }
            }
        }));

        GameActions.Bottom.StackPower(new EvePower(p, magicNumber, this));
    }

    public static class EvePower extends AnimatorPower
    {
        AnimatorCard eve;

        public EvePower(AbstractCreature owner, int amount, AnimatorCard eve)
        {
            super(owner, Eve.DATA);

            this.eve = eve;

            Initialize(amount);
        }

        @Override
        public void onAfterCardPlayed(AbstractCard usedCard)
        {
            super.onAfterCardPlayed(usedCard);

            GameActions.Bottom.DealDamageToRandomEnemy(eve, AbstractGameAction.AttackEffect.NONE)
            .SetDamageEffect(enemy ->
            {
                CardCrawlGame.sound.play("ATTACK_MAGIC_BEAM_SHORT");
                GameEffects.List.Add(new SmallLaserEffect(enemy.hb.cX, enemy.hb.cY, owner.hb.cX, owner.hb.cY));
                GameEffects.List.Add(new BorderFlashEffect(Color.SKY));
                return 0.2f;
            });
        }
    }
}