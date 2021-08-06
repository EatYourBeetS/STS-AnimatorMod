package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.orbs.animator.Water;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.RejuvenationPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class KokomiSangonomiya extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KokomiSangonomiya.class).SetSkill(2, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GenshinImpact);
    public static final int HP_HEAL_THRESHOLD = 40;

    public KokomiSangonomiya()
    {
        super(DATA);

        Initialize(0, 0, 6, 1);
        SetUpgrade(0, 0, -1);
        SetAffinity_Blue(2, 0, 0);
        SetAffinity_Light(1, 0, 0);

        SetExhaust(true);
        SetHealing(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
            GameActions.Bottom.VFX(VFX.WaterDome(player.hb.cX,(player.hb.y+player.hb.cY)/2));
            GameActions.Bottom.SFX(SFX.ANIMATOR_WATER_DOME);

            int totalHeal = 0;
            ArrayList<AbstractMonster> enemies = GameUtilities.GetEnemies(true);
            for (AbstractMonster mo : enemies)
            {
                int heal = mo.maxHealth - mo.currentHealth;
                GameActions.Bottom.Heal(p, mo, heal);
                totalHeal += heal;
            }

            int stacks = Math.floorDiv(totalHeal, magicNumber);
            Water waterOrb = new Water();
            waterOrb.IncreaseBasePassiveAmount(stacks);
            GameActions.Bottom.ChannelOrb(waterOrb);

            if (stacks > 0)
            {
                waterOrb.passiveAmount += stacks;
                if (totalHeal >= HP_HEAL_THRESHOLD && CombatStats.TryActivateLimited(cardID))
                {
                    GameActions.Bottom.StackPower(new RejuvenationPower(player, secondaryValue));
                }
            }


    }
}