package pinacolada.cards.pcl.colorless;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.special.GeassPower;
import pinacolada.utilities.PCLActions;

import java.util.ArrayList;

public class Lelouch extends PCLCard
{
    public static final PCLCardData DATA = Register(Lelouch.class)
            .SetSkill(3, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.ALL)
            .SetMaxCopies(1)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.CodeGeass);

    public Lelouch()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetCostUpgrade(-1);

        SetAffinity_Orange(1);
        SetAffinity_Blue(1);

        SetPurge(true);
        SetEthereal(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        int count = 0;
        for (AbstractCard c : player.hand.group)
        {
            if (c.uuid != uuid)
            {
                count += 1;
            }
        }

        SetUnplayable(count < magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ExhaustFromHand(name, magicNumber, true)
        .ShowEffect(true, true)
        .SetOptions(false, false, false)
        .AddCallback(info, (info2, cards) ->
        {
            if (cards.size() >= magicNumber)
            {
                final ArrayList<AbstractMonster> enemies = new ArrayList<>();
                for (AbstractMonster m2 : info2.Enemies)
                {
                    if (!m2.hasPower(GeassPower.POWER_ID))
                    {
                        enemies.add(m2);
                    }
                }

                if (enemies.size() > 0)
                {
                    PCLActions.Bottom.VFX(new BorderFlashEffect(Color.RED));
                    PCLActions.Bottom.SFX("MONSTER_COLLECTOR_DEBUFF");
                    for (AbstractMonster m2 : enemies)
                    {
                        PCLActions.Bottom.ApplyPower(player, new GeassPower(m2));
                    }
                }
            }
        });
    }
}