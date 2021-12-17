package pinacolada.cards.pcl.series.Fate;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

import java.util.concurrent.atomic.AtomicInteger;

public class Assassin extends PCLCard
{
    public static final PCLCardData DATA = Register(Assassin.class)
            .SetAttack(0, CardRarity.COMMON, PCLAttackType.Piercing)
            .SetSeriesFromClassPackage();
    public static final int DEBUFFS_COUNT = 3;

    public Assassin()
    {
        super(DATA);

        Initialize(2, 0, DEBUFFS_COUNT);
        SetUpgrade(2, 0);

        SetRetain(true);

        SetAffinity_Green(1, 0, 1);
        SetAffinity_Dark(1);

        SetHitCount(3);
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        if (c.type == CardType.ATTACK && player.hand.contains(this))
        {
            PCLActions.Bottom.MoveCard(this, player.hand, player.discardPile)
            .AddCallback(() -> PCLActions.Bottom.Draw(1));
        }
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        if (super.cardPlayable(m))
        {
            if (m == null)
            {
                for (AbstractMonster enemy : PCLGameUtilities.GetEnemies(true))
                {
                    if (PCLGameUtilities.GetDebuffsCount(enemy.powers) >= DEBUFFS_COUNT)
                    {
                        return true;
                    }
                }
            }
            else
            {
                return PCLGameUtilities.GetDebuffsCount(m) >= DEBUFFS_COUNT;
            }
        }

        return false;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        AtomicInteger i = new AtomicInteger();
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE).forEach(d -> {d.SetDamageEffect(e -> DamageEffect(e, i.get()));
        i.getAndIncrement();});
    }

    private float DamageEffect(AbstractCreature e, int index)
    {
        float x = e.hb.cX;
        float y = e.hb.cY - 60f * Settings.scale;
        float scale = 3;
        float dx;
        float dy;
        float angle;

        SFX.Play(SFX.ATTACK_SWORD, 0.85f + (0.2f * index));

        if (index % 3 == 0)
        {
            dx = 500;
            dy = 200;
            angle = 290;
        }
        else if (index % 3 == 1)
        {
            dx = -500;
            dy = 200;
            angle = -290;
        }
        else
        {
            dx = -500;
            dy = -200;
            angle = 120;
        }

        return PCLGameEffects.List.Add(new AnimatedSlashEffect(x, y, dx, dy, angle, scale, Color.VIOLET, Color.TEAL)).duration * 0.4f;
    }
}